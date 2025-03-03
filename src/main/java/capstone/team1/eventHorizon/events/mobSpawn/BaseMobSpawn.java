package capstone.team1.eventHorizon.events.mobSpawn;

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

/**
 * Parent class for mob spawning events.
 * Handles spawning mobs for all players or specific players.
 */
public abstract class BaseMobSpawn {
    protected final Plugin plugin;
    protected final Random random = new Random();

    // Spawning configuration
    EntityType mobType = EntityType.ZOMBIE; // Default mob type
    public int mobCount = 5; // Number of mobs to spawn per player
    public int maxSpawnRadius = 10; // Maximum horizontal radius around target location to spawn mobs
    public int minSpawnRadius = 5; // Minimum horizontal radius around target location to spawn mobs
    public int maxSpawnAttempts = 10; // Max attempts to find a safe location

    // Y-coordinate spawn range (relative to player)
    public int maxYRadius = 10; // Maximum vertical distance from player
    public int minYRadius = 0; // Minimum vertical distance from player

    // New spawn location configuration options
    public boolean allowCaveSpawns = true; // Allow spawning in underground caves
    public boolean allowWaterSpawns = false; // Allow spawning in water
    public boolean allowLavaSpawns = false; // Allow spawning in lava
    public int minY = 0; // Minimum Y coordinate for spawns
    public int maxY = 255; // Maximum Y coordinate for spawns

    // Player tracking
    protected final Map<UUID, Set<UUID>> playerMobs = new ConcurrentHashMap<>(); // Tracks mobs spawned per player
    protected final Map<UUID, Long> lastSpawnTime = new ConcurrentHashMap<>(); // Tracks last spawn time per player

    // Continuous spawning
    public BukkitTask continuousTask = null;
    public int spawnInterval = 60; // Seconds between spawns (default: 60 seconds)

    /**
     * Constructor for the MobSpawner.
     *
     * @param plugin Reference to your plugin
     */
    public BaseMobSpawn(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Constructor for the MobSpawner with specific mob type.
     *
     * @param plugin Reference to your plugin
     * @param defaultMobType The default mob type to spawn
     */
    public BaseMobSpawn(Plugin plugin, EntityType defaultMobType) {
        this.plugin = plugin;
        this.mobType = defaultMobType;
    }

    /**
     * Spawn mobs for all online players.
     *
     * @return Total number of mobs spawned
     */
    public int spawnForAllPlayers() {
        int totalSpawned = 0;
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            totalSpawned += spawnForPlayer(player).size();
        }
        return totalSpawned;
    }

    /**
     * Spawn mobs for a specific player.
     *
     * @param player The player to spawn mobs for
     * @return List of spawned entities
     */
    public List<Entity> spawnForPlayer(Player player) {
        List<Entity> spawnedEntities = new ArrayList<>();
        UUID playerUUID = player.getUniqueId();

        // Update last spawn time
        lastSpawnTime.put(playerUUID, System.currentTimeMillis());

        // Get or create the set of mobs for this player
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

    /**
     * Spawn mobs for an array of players.
     *
     * @param players Array of players to spawn mobs for
     * @return Total number of mobs spawned
     */
    public int spawnMobs(Player[] players) {
        int totalSpawned = 0;
        for (Player player : players) {
            totalSpawned += spawnForPlayer(player).size();
        }
        return totalSpawned;
    }

    /**
     * Start continuous spawning for all players.
     *
     * @return true if continuous spawning started, false if already running
     */
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
        }.runTaskTimer(plugin, 0, spawnInterval * 20L); // Convert seconds to ticks

        return true;
    }

    /**
     * Stop continuous spawning if active.
     */
    public void stopContinuousSpawning() {
        if (continuousTask != null) {
            continuousTask.cancel();
            continuousTask = null;
        }
    }

    /**
     * Find a safe location to spawn a mob near a player.
     *
     * @param player The player to spawn near
     * @return A safe location or null if none found
     */
    private Location getSafeSpawnLocation(Player player) {
        World world = player.getWorld();
        Location playerLoc = player.getLocation();
        int playerY = playerLoc.getBlockY();

        // Each attempt uses a new random offset
        for (int attempt = 0; attempt < maxSpawnAttempts; attempt++) {
            // Get horizontal offset
            int x = playerLoc.getBlockX() + getRandomHorizontalOffset();
            int z = playerLoc.getBlockZ() + getRandomHorizontalOffset();

            // Get vertical offset (relative to player)
            int yOffset = getRandomVerticalOffset();
            int targetY = playerY + yOffset;

            // Constrain to world limits and configured Y range
            targetY = Math.max(minY, Math.min(maxY, targetY));

            // Decide whether to attempt cave spawning
            boolean tryCaveSpawn = allowCaveSpawns && random.nextBoolean();

            Location spawnLoc = null;

            if (tryCaveSpawn) {
                // For cave spawning, look in the vicinity of the target Y
                spawnLoc = findCaveLocation(world, x, z, targetY, 5); // Look within 5 blocks of target Y
            }

            // If cave spawning failed or wasn't attempted, try surface spawning
            if (spawnLoc == null) {
                spawnLoc = findSurfaceLocation(world, x, z, targetY);
            }

            if (spawnLoc != null) {
                // Additional check to make sure the location is within the vertical radius limit
                int distanceY = Math.abs(spawnLoc.getBlockY() - playerY);
                if (distanceY >= minYRadius && distanceY <= maxYRadius) {
                    return spawnLoc;
                }
            }
        }

        return null;
    }

    /**
     * Find a valid cave location for spawning.
     *
     * @param world The world to search in
     * @param x X coordinate
     * @param z Z coordinate
     * @param targetY Target Y coordinate (center of search)
     * @param searchRange Range to search above and below targetY
     * @return A valid cave spawn location or null if none found
     */
    private Location findCaveLocation(World world, int x, int z, int targetY, int searchRange) {
        // Search around the target Y (both above and below)
        int minSearchY = Math.max(minY, targetY - searchRange);
        int maxSearchY = Math.min(maxY, targetY + searchRange);

        // Search for a cave near the target Y
        for (int y = targetY; y <= maxSearchY; y++) {
            Location loc = checkCaveBlock(world, x, y, z);
            if (loc != null) return loc;
        }

        // If not found above, look below
        for (int y = targetY - 1; y >= minSearchY; y--) {
            Location loc = checkCaveBlock(world, x, y, z);
            if (loc != null) return loc;
        }

        return null;
    }

    /**
     * Check if a specific block is a valid cave location.
     *
     * @param world The world to check in
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return A valid spawn location or null
     */
    private Location checkCaveBlock(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        Block blockAbove = world.getBlockAt(x, y + 1, z);
        Block blockBelow = world.getBlockAt(x, y - 1, z);

        // Check if this is a valid cave location (air with solid block below)
        if (block.getType() == Material.AIR &&
                blockBelow.getType().isSolid()) {

            // Check liquid restrictions
            if (isInvalidLiquidLocation(block)) {
                return null;
            }

            // Create a centered spawn location with whole-number Y coordinate
            // Make sure the entity spawns ON TOP of the solid block, not inside it
            return new Location(world, x + 0.5, y, z + 0.5);
        }

        return null;
    }

    /**
     * Find a valid surface location for spawning.
     *
     * @param world The world to search in
     * @param x X coordinate
     * @param z Z coordinate
     * @param preferredY The preferred Y coordinate (will try to find closest valid location)
     * @return A valid surface spawn location or null if none found
     */
    private Location findSurfaceLocation(World world, int x, int z, int preferredY) {
        // Find the highest block at this x,z
        int highestY = world.getHighestBlockYAt(x, z);

        // If the highest block is below minY or above maxY, return null
        if (highestY < minY || highestY > maxY) {
            return null;
        }

        // Get the highest block and the one above it
        Block highestBlock = world.getBlockAt(x, highestY, z);
        Block blockAbove = world.getBlockAt(x, highestY + 1, z);

        // Check if this is a valid surface location (air block above solid block)
        if (blockAbove.getType() == Material.AIR && highestBlock.getType().isSolid()) {
            // Check liquid restrictions
            if (isInvalidLiquidLocation(blockAbove) || isInvalidLiquidLocation(highestBlock)) {
                return null;
            }

            // Create a centered spawn location ON TOP of the highest solid block
            return new Location(world, x + 0.5, highestY + 1, z + 0.5);
        }

        return null;
    }

    /**
     * Check if a block location is invalid due to liquid restrictions.
     *
     * @param block The block to check
     * @return true if the location is invalid, false if valid
     */
    private boolean isInvalidLiquidLocation(Block block) {
        Material type = block.getType();

        // Check water spawning restrictions
        if (type == Material.WATER && !allowWaterSpawns) {
            return true;
        }

        // Check lava spawning restrictions
        if (type == Material.LAVA && !allowLavaSpawns) {
            return true;
        }

        return false;
    }

    /**
     * Generate a random horizontal offset within min/max spawn radius.
     *
     * @return Random offset value
     */
    protected int getRandomHorizontalOffset() {
        int range = maxSpawnRadius - minSpawnRadius;
        int offset = random.nextInt(range * 2 + 1) + minSpawnRadius;
        return random.nextBoolean() ? offset : -offset;
    }

    /**
     * Generate a random vertical offset within min/max Y radius.
     *
     * @return Random vertical offset
     */
    protected int getRandomVerticalOffset() {
        int range = maxYRadius - minYRadius;
        if (range <= 0) return 0;

        int offset = random.nextInt(range + 1) + minYRadius;
        return random.nextBoolean() ? offset : -offset;
    }

    /**
     * Spawn a mob at the specified location.
     *
     * @param location The location to spawn at
     * @return The spawned entity or null if spawn failed
     */
    protected Entity spawnMob(Location location) {
        try {
            // Create a new location at exact block center with the correct height
            Location centeredLocation = new Location(
                    location.getWorld(),
                    Math.floor(location.getX()) + 0.5,  // Center X
                    location.getY(),                    // Keep exact Y
                    Math.floor(location.getZ()) + 0.5,  // Center Z
                    location.getYaw(),
                    location.getPitch()
            );

            // Verify there's enough space above for the entity
            double clearanceHeight = 2.0; // Default clearance height for most mobs
            if (mobType == EntityType.WOLF ||
                    mobType == EntityType.FOX ||
                    mobType == EntityType.CAT) {
                clearanceHeight = 1.5; // Smaller clearance for smaller mobs
            } else if (mobType == EntityType.ENDERMAN ||
                    mobType == EntityType.IRON_GOLEM) {
                clearanceHeight = 3.0; // Taller clearance for taller mobs
            }

            // Check if there's enough vertical space
            for (double y = 0; y < clearanceHeight; y += 0.5) {
                Block checkBlock = centeredLocation.clone().add(0, y, 0).getBlock();
                if (checkBlock.getType() != Material.AIR) {
                    // Not enough vertical space, try to adjust
                    if (y < 0.5) {
                        // If we can't even fit the bottom of the entity, abort
                        return null;
                    }
                    break;
                }
            }

            return centeredLocation.getWorld().spawnEntity(centeredLocation, mobType, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to spawn " + mobType + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Clean up mobs spawned for a specific player.
     *
     * @param playerUUID UUID of the player
     */
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

    /**
     * Called after a mob is spawned. Override to customize mob behavior.
     *
     * @param entity The spawned entity
     * @param player The player the mob was spawned for
     */
    protected void onMobSpawned(Entity entity, Player player) {
        // Override in child classes to customize the mob
    }

    // Builder-style setters for easy configuration

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

    // New setter methods for the added features

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