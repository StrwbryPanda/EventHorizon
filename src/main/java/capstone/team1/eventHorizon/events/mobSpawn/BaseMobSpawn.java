package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseMobSpawn extends BaseEvent
{
    protected final Plugin plugin;
    protected final Random random = new Random();

    // Spawning configuration
    private static final int DEFAULT_MOB_COUNT = 5;
    private static final int DEFAULT_MAX_SPAWN_RADIUS = 10;
    private static final int DEFAULT_MIN_SPAWN_RADIUS = 5;
    private static final int DEFAULT_MAX_SPAWN_ATTEMPTS = 10;
    private static final int DEFAULT_MAX_Y_RADIUS = 10;
    private static final int DEFAULT_MIN_Y_RADIUS = 0;
    private static final int DEFAULT_MIN_Y = 0;
    private static final int DEFAULT_MAX_Y = 255;
    private static final int DEFAULT_SPAWN_INTERVAL = 60;

    EntityType mobType = EntityType.ZOMBIE;
    public int mobCount = DEFAULT_MOB_COUNT;
    public int maxSpawnRadius = DEFAULT_MAX_SPAWN_RADIUS;
    public int minSpawnRadius = DEFAULT_MIN_SPAWN_RADIUS;
    public int maxSpawnAttempts = DEFAULT_MAX_SPAWN_ATTEMPTS;
    public int maxYRadius = DEFAULT_MAX_Y_RADIUS;
    public int minYRadius = DEFAULT_MIN_Y_RADIUS;
    public boolean allowCaveSpawns = true;
    public boolean allowWaterSpawns = false;
    public boolean allowLavaSpawns = false;
    public int minY = DEFAULT_MIN_Y;
    public int maxY = DEFAULT_MAX_Y;

    protected final Map<UUID, Set<UUID>> playerMobs = new ConcurrentHashMap<>();
    protected final Map<UUID, Long> lastSpawnTime = new ConcurrentHashMap<>();

    public BukkitTask continuousTask = null;
    public int spawnInterval = DEFAULT_SPAWN_INTERVAL;

    public BaseMobSpawn(Plugin plugin, EventClassification classification) {
        super(classification);
        this.plugin = plugin;
    }

    public BaseMobSpawn(Plugin plugin, EntityType defaultMobType) {

        super(EventClassification.NEUTRAL);
        this.plugin = plugin;
        this.mobType = defaultMobType;
    }

    public BaseMobSpawn(Plugin plugin, EntityType defaultMobType, EventClassification classification) {
        super(classification);
        this.plugin = plugin;
        this.mobType = defaultMobType;
    }

    public int spawnForAllPlayers() {
        int totalSpawned = 0;
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            totalSpawned += spawnForPlayer(player).size();
        }
        return totalSpawned;
    }

    public List<Entity> spawnForPlayer(Player player) {
        List<Entity> spawnedEntities = new ArrayList<>();
        UUID playerUUID = player.getUniqueId();
        lastSpawnTime.put(playerUUID, System.currentTimeMillis());
        Set<UUID> playerEntitySet = playerMobs.computeIfAbsent(playerUUID, k -> new HashSet<>());

        for (int i = 0; i < mobCount; i++) {
            Location spawnLoc = getSafeSpawnLocation(player);
            if (spawnLoc != null) {
                Entity entity = spawnMob(spawnLoc);
                if (entity != null) {
                    spawnedEntities.add(entity);
                    playerEntitySet.add(entity.getUniqueId());
                    onMobSpawned(entity, player);
                }
            }
        }
        return spawnedEntities;
    }

    public int spawnMobs(Player[] players) {
        int totalSpawned = 0;
        for (Player player : players) {
            totalSpawned += spawnForPlayer(player).size();
        }
        return totalSpawned;
    }

    public boolean startContinuousSpawning() {
        if (continuousTask != null) {
            return false;
        }

        continuousTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    spawnForPlayer(player);
                }
            }
        }.runTaskTimer(plugin, 0, spawnInterval * 20L);

        return true;
    }

    public void stopContinuousSpawning() {
        if (continuousTask != null) {
            continuousTask.cancel();
            continuousTask = null;
        }
    }

    private Location getSafeSpawnLocation(Player player) {
        World world = player.getWorld();
        Location playerLoc = player.getLocation();
        int playerY = playerLoc.getBlockY();

        for (int attempt = 0; attempt < maxSpawnAttempts; attempt++) {
            int x = playerLoc.getBlockX() + getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int z = playerLoc.getBlockZ() + getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int yOffset = getRandomOffset(minYRadius, maxYRadius);
            int targetY = Math.max(minY, Math.min(maxY, playerY + yOffset));
            boolean tryCaveSpawn = allowCaveSpawns && random.nextBoolean();
            Location spawnLoc = tryCaveSpawn ? findCaveLocation(world, x, z, targetY, 5) : findSurfaceLocation(world, x, z, targetY);

            if (spawnLoc != null && Math.abs(spawnLoc.getBlockY() - playerY) >= minYRadius && Math.abs(spawnLoc.getBlockY() - playerY) <= maxYRadius) {
                return spawnLoc;
            }
        }
        return null;
    }

    private Location findCaveLocation(World world, int x, int z, int targetY, int searchRange) {
        int minSearchY = Math.max(minY, targetY - searchRange);
        int maxSearchY = Math.min(maxY, targetY + searchRange);

        for (int y = targetY; y <= maxSearchY; y++) {
            Location loc = checkCaveBlock(world, x, y, z);
            if (loc != null) return loc;
        }

        for (int y = targetY - 1; y >= minSearchY; y--) {
            Location loc = checkCaveBlock(world, x, y, z);
            if (loc != null) return loc;
        }
        return null;
    }

    private Location checkCaveBlock(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        Block blockBelow = world.getBlockAt(x, y - 1, z);

        if (block.getType() == Material.AIR && blockBelow.getType().isSolid() && !isInvalidLiquidLocation(block)) {
            return new Location(world, x + 0.5, y, z + 0.5);
        }
        return null;
    }

    private Location findSurfaceLocation(World world, int x, int z, int preferredY) {
        int highestY = world.getHighestBlockYAt(x, z);
        if (highestY < minY || highestY > maxY) {
            return null;
        }

        Block highestBlock = world.getBlockAt(x, highestY, z);
        Block blockAbove = world.getBlockAt(x, highestY + 1, z);

        if (blockAbove.getType() == Material.AIR && highestBlock.getType().isSolid() && !isInvalidLiquidLocation(blockAbove) && !isInvalidLiquidLocation(highestBlock)) {
            return new Location(world, x + 0.5, highestY + 1, z + 0.5);
        }
        return null;
    }

    private boolean isInvalidLiquidLocation(Block block) {
        Material type = block.getType();
        return (type == Material.WATER && !allowWaterSpawns) || (type == Material.LAVA && !allowLavaSpawns);
    }

    private int getRandomOffset(int min, int max) {
        int range = max - min;
        int offset = random.nextInt(range * 2 + 1) + min;
        return random.nextBoolean() ? offset : -offset;
    }

    protected Entity spawnMob(Location location) {
        try {
            Location centeredLocation = new Location(location.getWorld(), Math.floor(location.getX()) + 0.5, location.getY(), Math.floor(location.getZ()) + 0.5, location.getYaw(), location.getPitch());
            double clearanceHeight = (mobType == EntityType.WOLF || mobType == EntityType.FOX || mobType == EntityType.CAT) ? 1.5 : (mobType == EntityType.ENDERMAN || mobType == EntityType.IRON_GOLEM) ? 3.0 : 2.0;

            for (double y = 0; y < clearanceHeight; y += 0.5) {
                if (centeredLocation.clone().add(0, y, 0).getBlock().getType() != Material.AIR) {
                    if (y < 0.5) return null;
                    break;
                }
            }
            return centeredLocation.getWorld().spawnEntity(centeredLocation, mobType, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to spawn " + mobType + " at " + location + ": " + e.getMessage());
            return null;
        }
    }

    public void cleanupPlayerMobs(UUID playerUUID) {
        Set<UUID> mobsToRemove = playerMobs.get(playerUUID);
        if (mobsToRemove != null) {
            for (UUID mobUUID : mobsToRemove) {
                Entity entity = Bukkit.getEntity(mobUUID);
                if (entity != null) {
                    entity.remove();
                }
            }
            playerMobs.remove(playerUUID);
        }
        lastSpawnTime.remove(playerUUID);
    }

    protected void onMobSpawned(Entity entity, Player player) {
    }

    public BaseMobSpawn setMobType(EntityType mobType) {
        this.mobType = mobType;
        return this;
    }

    public BaseMobSpawn setMobCount(int count) {
        this.mobCount = Math.max(1, count);
        return this;
    }

    public BaseMobSpawn setMaxSpawnRadius(int radius) {
        this.maxSpawnRadius = Math.max(5, radius);
        return this;
    }

    public BaseMobSpawn setMinSpawnRadius(int radius) {
        this.minSpawnRadius = Math.max(1, Math.min(radius, maxSpawnRadius - 1));
        return this;
    }

    public BaseMobSpawn setYRadius(int min, int max) {
        this.minYRadius = Math.max(0, min);
        this.maxYRadius = Math.max(minYRadius, max);
        return this;
    }

    public BaseMobSpawn setMaxSpawnAttempts(int attempts) {
        this.maxSpawnAttempts = Math.max(1, attempts);
        return this;
    }

    public BaseMobSpawn setSpawnInterval(int seconds) {
        this.spawnInterval = Math.max(1, seconds);
        return this;
    }

    public BaseMobSpawn setAllowCaveSpawns(boolean allow) {
        this.allowCaveSpawns = allow;
        return this;
    }

    public BaseMobSpawn setAllowWaterSpawns(boolean allow) {
        this.allowWaterSpawns = allow;
        return this;
    }

    public BaseMobSpawn setAllowLavaSpawns(boolean allow) {
        this.allowLavaSpawns = allow;
        return this;
    }

    public BaseMobSpawn setYRange(int min, int max) {
        this.minY = Math.max(0, min);
        this.maxY = Math.min(255, Math.max(min, max));
        return this;
    }
}