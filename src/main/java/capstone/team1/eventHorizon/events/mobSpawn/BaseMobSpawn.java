package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class BaseMobSpawn extends BaseEvent
{
    protected final Plugin plugin;
    protected final Random random = new Random();

    // Default configuration values
    private static final int DEFAULT_MOB_COUNT = 5;
    private static final int DEFAULT_MAX_SPAWN_RADIUS = 10;
    private static final int DEFAULT_MIN_SPAWN_RADIUS = 5;
    private static final int DEFAULT_MAX_SPAWN_ATTEMPTS = 10;
    private static final int DEFAULT_SPAWN_INTERVAL = 60;
    private static final double DEFAULT_WIDTH_CLEARANCE = 1;
    private static final double DEFAULT_HEIGHT_CLEARANCE = 2;
    private static final int DEFAULT_GROUP_SPACING = 3;

    // Entity properties
    EntityType mobType = EntityType.ZOMBIE;
    public int mobCount = DEFAULT_MOB_COUNT;
    public int maxSpawnRadius = DEFAULT_MAX_SPAWN_RADIUS;
    public int minSpawnRadius = DEFAULT_MIN_SPAWN_RADIUS;
    public int maxSpawnAttempts = DEFAULT_MAX_SPAWN_ATTEMPTS;
    public int maxYRadius = DEFAULT_MAX_SPAWN_RADIUS;
    public int minYRadius = DEFAULT_MIN_SPAWN_RADIUS;
    public double widthClearance = DEFAULT_WIDTH_CLEARANCE;
    public double heightClearance = DEFAULT_HEIGHT_CLEARANCE;
    public int groupSpacing = DEFAULT_GROUP_SPACING;

    // Flags
    public boolean surfaceOnlySpawning = false;
    public boolean allowWaterSpawns = false;
    public boolean allowLavaSpawns = false;
    public boolean groupSpawning = false;

    // Task management
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

    @Override
    public void execute() {
        spawnForAllPlayers();
    }

    protected void onMobSpawned(Entity entity, Player player) {
    }

    public int spawnForAllPlayers() {
        int totalSpawned = 0;
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            totalSpawned += spawnForPlayer(player).size();
        }
        return totalSpawned;
    }

    public List<Entity> spawnForPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }
        return groupSpawning ? spawnGroupForPlayer(player) : spawnSpreadForPlayer(player);
    }

    // Spawns mobs spread around the player
    public List<Entity> spawnSpreadForPlayer(Player player) {
        List<Entity> spawnedEntities = new ArrayList<>();
        World world = player.getWorld();
        Location playerLocation = player.getLocation();

        int attempts = 0;
        int spawned = 0;

        while (spawned < mobCount && attempts < maxSpawnAttempts) {
            attempts++;

            // Calculate random position within spawn radius
            int xOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int zOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int yOffset = surfaceOnlySpawning ? 0 : getRandomOffset(minYRadius, maxYRadius);

            // Create spawn location
            Location spawnLocation = playerLocation.clone().add(xOffset, yOffset, zOffset);

            // For surface only spawning, find the highest block at this X,Z
            if (surfaceOnlySpawning) {
                int highestY = world.getHighestBlockYAt(spawnLocation);
                spawnLocation.setY(highestY + 1); // Set Y to one above the highest block
            }

            // Check if the location is safe to spawn
            if (isSafeLocation(spawnLocation)) {
                Entity entity = world.spawnEntity(spawnLocation, mobType);
                spawnedEntities.add(entity);
                spawned++;
            }
        }

        return spawnedEntities;
    }

    // Spawns a group of mobs near the player
    public List<Entity> spawnGroupForPlayer(Player player) {
        List<Entity> spawnedEntities = new ArrayList<>();
        World world = player.getWorld();
        Location playerLocation = player.getLocation();

        // Try to find a suitable location for the group
        Location groupCenter = null;
        int attempts = 0;

        while (groupCenter == null && attempts < maxSpawnAttempts) {
            attempts++;

            // Calculate random position within spawn radius for the group center
            int xOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int zOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int yOffset = surfaceOnlySpawning ? 0 : getRandomOffset(minYRadius, maxYRadius);

            // Create potential group center location
            Location potentialCenter = playerLocation.clone().add(xOffset, yOffset, zOffset);

            // For surface only spawning, find the highest block at this X,Z
            if (surfaceOnlySpawning) {
                int highestY = world.getHighestBlockYAt(potentialCenter);
                potentialCenter.setY(highestY + 1);
            }

            // Check if location is safe
            if (isSafeLocation(potentialCenter)) {
                groupCenter = potentialCenter;
            }
        }

        // If we couldn't find a suitable group center, return empty list
        if (groupCenter == null) {
            return spawnedEntities;
        }

        // Spawn the mobs in a group around the center
        int spawned = 0;
        attempts = 0;

        while (spawned < mobCount && attempts < maxSpawnAttempts * 2) {
            attempts++;

            // Calculate a close position to the group center
            int xOffset = random.nextInt(groupSpacing * 2 + 1) - groupSpacing;
            int zOffset = random.nextInt(groupSpacing * 2 + 1) - groupSpacing;

            // Create spawn location
            Location spawnLocation = groupCenter.clone().add(xOffset, 0, zOffset);

            // For surface only spawning, adjust Y to the highest block
            if (surfaceOnlySpawning) {
                int highestY = world.getHighestBlockYAt(spawnLocation);
                spawnLocation.setY(highestY + 1);
            }

            // Check if the location is safe to spawn
            if (isSafeLocation(spawnLocation)) {
                Entity entity = world.spawnEntity(spawnLocation, mobType);
                spawnedEntities.add(entity);
                spawned++;
            }
        }

        return spawnedEntities;
    }

    //  Continuous task management
    public boolean startContinuousTask() {
        // Check if task is already running
        if (continuousTask != null && !continuousTask.isCancelled()) {
            return false;
        }

        // Use BukkitRunnable for continuous task that spawns mobs for all players
        continuousTask = new BukkitRunnable() {
            @Override
            public void run() {
                spawnForAllPlayers();
            }
        }.runTaskTimer(plugin, 20L, spawnInterval * 20L);

        return true;
    }

    public boolean stopContinuousTask() {
        // Check if there's a task to stop
        if (continuousTask == null || continuousTask.isCancelled()) {
            return false;
        }

        // Cancel the task
        continuousTask.cancel();
        continuousTask = null;

        return true;
    }

    // Clearance checks
    private boolean isSafeLocation(Location location) {
        World world = location.getWorld();
        if (location.getBlockY() < world.getMinHeight() || location.getBlockY() >= world.getMaxHeight()) {
            return false;
        }

        Block block = location.getBlock();
        Block blockBelow = location.clone().subtract(0, 1, 0).getBlock();

        return isSafeBlock(block) &&
                blockBelow.getType().isSolid() &&
                checkBlockClearance(location);
    }

    private boolean isLiquidLocation(Block block) {
        Material type = block.getType();
        return (type == Material.WATER && allowWaterSpawns) || (type == Material.LAVA && allowLavaSpawns);
    }

    private boolean isSafeBlock(Block block) {
        return block.getType() == Material.AIR || isLiquidLocation(block);
    }

    private boolean checkBlockClearance(Location location) {
        World world = location.getWorld();
        int baseX = location.getBlockX();
        int baseY = location.getBlockY();
        int baseZ = location.getBlockZ();

        int heightBlocks = (int)Math.ceil(heightClearance);
        int widthBlocks = (int)Math.ceil(widthClearance);

        // Check World height limit
        if (baseY + heightClearance >= world.getMaxHeight()) {
            return false;
        }

        for (int y = 0; y < heightBlocks; y++) {
            int checkY = baseY + y;

            for (int x = -widthBlocks; x <= widthBlocks; x++) {
                for (int z = -widthBlocks; z <= widthBlocks; z++) {
                    if (!isSafeBlock(world.getBlockAt(baseX + x, checkY, baseZ + z))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private int getRandomOffset(int min, int max) {
        int range = max - min;
        int offset = random.nextInt(range + 1) + min;
        return random.nextBoolean() ? offset : -offset;
    }

    public BaseMobSpawn setMobType(EntityType mobType) {
        this.mobType = mobType;
        return this;
    }

    // Setters
    public BaseMobSpawn setMobCount(int count) {
        this.mobCount = count;
        return this;
    }

    public BaseMobSpawn setMaxSpawnRadius(int radius) {
        this.maxSpawnRadius = radius;
        return this;
    }

    public BaseMobSpawn setMinSpawnRadius(int radius) {
        this.minSpawnRadius = radius;
        return this;
    }

    public BaseMobSpawn setMaxSpawnAttempts(int attempts) {
        this.maxSpawnAttempts = attempts;
        return this;
    }

    public BaseMobSpawn setMaxYRadius(int radius) {
        this.maxYRadius = radius;
        return this;
    }

    public BaseMobSpawn setMinYRadius(int radius) {
        this.minYRadius = radius;
        return this;
    }

    public BaseMobSpawn setWidthClearance(double clearance) {
        this.widthClearance = clearance;
        return this;
    }

    public BaseMobSpawn setHeightClearance(double clearance) {
        this.heightClearance = clearance;
        return this;
    }

    public BaseMobSpawn setGroupSpacing(int spacing) {
        this.groupSpacing = spacing;
        return this;
    }

    public BaseMobSpawn setSurfaceOnlySpawning(boolean surfaceOnly) {
        this.surfaceOnlySpawning = surfaceOnly;
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

    public BaseMobSpawn setGroupSpawning(boolean groupSpawn) {
        this.groupSpawning = groupSpawn;
        return this;
    }

    public BaseMobSpawn setSpawnInterval(int seconds) {
        this.spawnInterval = seconds;
        return this;
    }
}