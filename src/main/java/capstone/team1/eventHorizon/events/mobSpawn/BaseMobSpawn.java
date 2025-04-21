package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static capstone.team1.eventHorizon.utility.MsgUtility.log;
import static capstone.team1.eventHorizon.utility.MsgUtility.warning;

/**
 * Base class for implementing mob spawning events in Minecraft.
 * This class provides comprehensive functionality for spawning mobs around players
 * with various configurable parameters such as spawn radius, mob types, and spawning behavior.
 *
 * The class supports:
 * - Single or continuous spawning
 * - Group or spread spawning
 * - Surface-only or 3D space spawning
 * - Multiple mob types with random selection
 * - Safe location detection for spawning
 *
 * @see BaseEvent
 * @see EventClassification
 */
public abstract class BaseMobSpawn extends BaseEvent {
    protected final Plugin plugin;
    protected final Random random = new Random();
    protected final NamespacedKey key;

    // Default configuration values
    /** Default number of mobs to spawn per player */
    private static final int DEFAULT_MOB_COUNT = 5;
    /** Maximum distance from player where mobs can spawn */
    private static final int DEFAULT_MAX_SPAWN_RADIUS = 20;
    /** Minimum distance from player where mobs can spawn */
    private static final int DEFAULT_MIN_SPAWN_RADIUS = 3;
    /** Maximum vertical distance from player where mobs can spawn */
    private static final int DEFAULT_MAX_Y_RADIUS = 20;
    /** Minimum vertical distance from player where mobs can spawn */
    private static final int DEFAULT_MIN_Y_RADIUS = 3;
    /** Maximum number of attempts to find a valid spawn location */
    private static final int DEFAULT_MAX_SPAWN_ATTEMPTS = 20;
    /** Required horizontal clearance for mob spawning */
    private static final double DEFAULT_WIDTH_CLEARANCE = 1;
    /** Required vertical clearance for mob spawning */
    private static final double DEFAULT_HEIGHT_CLEARANCE = 2;
    /** Distance between mobs when spawning in groups */
    private static final int DEFAULT_GROUP_SPACING = 3;
    /** Interval between continuous spawns in seconds */
    private static final int DEFAULT_SPAWN_INTERVAL = 60;
    /** Whether mobs only spawn on surface blocks */
    private static final boolean DEFAULT_SURFACE_ONLY_SPAWNING = false;
    /** Whether mobs can spawn in water */
    private static final boolean DEFAULT_ALLOW_WATER_SPAWNS = false;
    /** Whether mobs can spawn in lava */
    private static final boolean DEFAULT_ALLOW_LAVA_SPAWNS = false;
    /** Whether mobs spawn in groups or spread out */
    private static final boolean DEFAULT_USE_GROUP_SPAWNING = false;
    /** Whether spawning continues at intervals */
    private static final boolean DEFAULT_USE_CONTINUOUS_SPAWNING = false;
    /** Whether to randomly select from multiple mob types */
    private static final boolean DEFAULT_USE_RANDOM_MOB_TYPES = false;

    // Entity properties
    /** The type of mob to spawn */
    protected EntityType mobType = EntityType.ZOMBIE;
    /** List of possible mob types to spawn */
    protected List<EntityType> mobTypes = new ArrayList<>();
    /** Number of mobs to spawn per player */
    protected int mobCount = DEFAULT_MOB_COUNT;
    /** Maximum distance from player where mobs can spawn */
    protected int maxSpawnRadius = DEFAULT_MAX_SPAWN_RADIUS;
    /** Minimum distance from player where mobs can spawn */
    protected int minSpawnRadius = DEFAULT_MIN_SPAWN_RADIUS;
    /** Maximum vertical distance from player where mobs can spawn */
    protected int maxYRadius = DEFAULT_MAX_Y_RADIUS;
    /** Minimum vertical distance from player where mobs can spawn */
    protected int minYRadius = DEFAULT_MIN_Y_RADIUS;
    /** Maximum number of attempts to find a valid spawn location */
    protected int maxSpawnAttempts = DEFAULT_MAX_SPAWN_ATTEMPTS;
    /** Required horizontal clearance for mob spawning */
    protected double widthClearance = DEFAULT_WIDTH_CLEARANCE;
    /** Required vertical clearance for mob spawning */
    protected double heightClearance = DEFAULT_HEIGHT_CLEARANCE;
    /** Distance between mobs when spawning in groups */
    protected int groupSpacing = DEFAULT_GROUP_SPACING;
    /** Number of mobs spawned in last execution */
    private int lastSpawnCount = 0;

    // Flags
    /** Whether mobs only spawn on surface blocks */
    protected boolean surfaceOnlySpawning = DEFAULT_SURFACE_ONLY_SPAWNING;
    /** Whether mobs can spawn in water */
    protected boolean allowWaterSpawns = DEFAULT_ALLOW_WATER_SPAWNS;
    /** Whether mobs can spawn in lava */
    protected boolean allowLavaSpawns = DEFAULT_ALLOW_LAVA_SPAWNS;
    /** Whether mobs spawn in groups or spread out */
    protected boolean useGroupSpawning = DEFAULT_USE_GROUP_SPAWNING;
    /** Whether spawning continues at intervals */
    protected boolean useContinuousSpawning = DEFAULT_USE_CONTINUOUS_SPAWNING;
    /** Whether to randomly select from multiple mob types */
    protected boolean useRandomMobTypes = DEFAULT_USE_RANDOM_MOB_TYPES;

    // Task management
    /** Task for continuous spawning */
    protected BukkitTask continuousTask = null;
    /** Interval between continuous spawns in seconds */
    protected int spawnInterval = DEFAULT_SPAWN_INTERVAL;

    // Constructors
    /**
     * Constructs a mob spawn event with specified classification and name.
     *
     * @param classification The event classification (NEUTRAL, HOSTILE, etc.)
     * @param eventName The unique name for this event
     */
    public BaseMobSpawn(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);
    }
    /**
     * Constructs a mob spawn event with a default mob type and name.
     * The event is classified as NEUTRAL by default.
     *
     * @param defaultMobType The default entity type to spawn
     * @param eventName The unique name for this event
     */
    public BaseMobSpawn(EntityType defaultMobType, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.mobType = defaultMobType;
        this.mobTypes.add(defaultMobType);
        this.key = new NamespacedKey(plugin, this.eventName);
    }
    /**
     * Constructs a mob spawn event with specified mob type, classification, and name.
     *
     * @param defaultMobType The default entity type to spawn
     * @param classification The event classification (NEUTRAL, HOSTILE, etc.)
     * @param eventName The unique name for this event
     */
    public BaseMobSpawn(EntityType defaultMobType, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.mobType = defaultMobType;
        this.mobTypes.add(defaultMobType);
        this.key = new NamespacedKey(plugin, this.eventName);
    }
    /**
     * Constructs a mob spawn event with multiple mob types and specified classification.
     * Enables random mob type selection by default.
     *
     * @param mobTypes List of entity types that can be spawned
     * @param classification The event classification (NEUTRAL, HOSTILE, etc.)
     * @param eventName The unique name for this event
     */
    public BaseMobSpawn(List<EntityType> mobTypes, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.mobTypes.addAll(mobTypes);
        this.useRandomMobTypes = true;
        this.key = new NamespacedKey(plugin, this.eventName);

        // Set default mob type to first in list
        if (!mobTypes.isEmpty()) {
            this.mobType = mobTypes.getFirst();
        } else {
            this.mobTypes.add(EntityType.ZOMBIE);
        }
    }

    /**
     * Spawns mobs for all online players based on the current configuration.
     * If continuous spawning is enabled, starts a repeating task.
     * Otherwise, performs a single spawn cycle.
     */
    @Override
    public void execute() {
        try {
            this.lastSpawnCount = 0;

            if (useContinuousSpawning) {
                // Start continuous task for ongoing spawning
                boolean started = startContinuousTask();
                if (started) {
                    if (useRandomMobTypes) {
                        log("Event " + eventName +
                                " started continuous spawning of random mobs" +
                                " with interval of " + spawnInterval + " seconds");
                    } else {
                        log("Event " + eventName +
                                " started continuous spawning of " + mobType.toString() +
                                " mobs with interval of " + spawnInterval + " seconds");
                    }
                } else {
                    log("Event " + eventName +
                            " tried to start continuous spawning but it was already running");
                }
            } else {
                // Do a one-time spawn for all players
                int spawned = spawnForAllPlayers();
                this.lastSpawnCount = spawned;

                if (useRandomMobTypes) {
                    log("Event " + eventName +
                            " spawned " + spawned + " random mobs across " +
                            plugin.getServer().getOnlinePlayers().size() +
                            " players");
                } else {
                    log("Event " + eventName +
                            " spawned " + spawned + " " + mobType.toString() +
                            " mobs across " + plugin.getServer().getOnlinePlayers().size() +
                            " players");
                }
            }
        } catch (Exception e) {
            warning("Error spawning mobs in " + eventName + ": " + e.getMessage());
        }
    }

    /**
     * Terminates the event by stopping any continuous spawning tasks.
     * Logs appropriate messages based on whether the event was using random mob types
     * and whether the task was successfully stopped.
     */
    @Override
    public void terminate() {
        boolean stopped = stopContinuousTask();

        if (stopped) {
            if (useRandomMobTypes) {
                log("Event " + eventName + " stopped continuous spawning of random mobs");
            } else {
                log("Event " + eventName + " stopped continuous spawning of " + mobType.toString() + " mobs");
            }
        } else {
            warning("Event " + eventName + " tried to stop continuous spawning but it was already stopped");
        }
    }

    /**
     * Starts a continuous task that spawns mobs at regular intervals.
     * The task runs every {@link #spawnInterval} seconds.
     *
     * @return true if the task was successfully started, false if a task is already running
     */
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

    /**
     * Stops the continuous spawning task if one is running.
     *
     * @return true if a task was stopped, false if no task was running
     */
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

    /**
     * Hook method called when a mob is spawned.
     * Can be overridden by child classes to implement custom behavior.
     *
     * @param entity The spawned entity
     * @param player The player for whom the mob was spawned
     */
    protected void onMobSpawned(Entity entity, Player player) {
    }

    /**
     * Spawns mobs for all online players.
     * Logs the spawn results for each player and calls {@link #onMobSpawned}
     * for each spawned entity.
     *
     * @return The total number of mobs spawned across all players
     */
    public int spawnForAllPlayers() {
        int totalSpawned = 0;
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        for (Player player : players) {
            List<Entity> spawnedEntities = spawnForPlayer(player);
            int playerSpawnCount = spawnedEntities.size();
            totalSpawned += playerSpawnCount;

            if (useRandomMobTypes) {
                log("Spawned " + playerSpawnCount + " random mobs for player " + player.getName());
            } else {
                log("Spawned " + playerSpawnCount + " " + mobType.toString() +
                        " for player " + player.getName());
            }

            // Optional hook for child classes to implement additional logic
            for (Entity entity : spawnedEntities) {
                onMobSpawned(entity, player);
            }
        }
        return totalSpawned;
    }

    /**
     * Spawns mobs for a specific player based on configuration settings.
     * Uses either group or spread spawning depending on {@link #useGroupSpawning}.
     *
     * @param player The player to spawn mobs for
     * @return List of spawned entities, or empty list if player is null or offline
     */
    public List<Entity> spawnForPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }
        return useGroupSpawning ? spawnGroupForPlayer(player) : spawnSpreadForPlayer(player);
    }

    /**
     * Spawns mobs spread out around a player within the configured radius.
     * Each mob is spawned at a random location within the spawn radius,
     * checking for safe locations before spawning.
     *
     * @param player The player to spawn mobs around
     * @return List of successfully spawned entities
     */
    public List<Entity> spawnSpreadForPlayer(Player player) {
        List<Entity> spawnedEntities = new ArrayList<>();
        World world = player.getWorld();
        Location playerLocation = player.getLocation();

        int attempts = 0;
        int spawned = 0;

        while (spawned < mobCount && attempts < maxSpawnAttempts) {
            attempts++;

            // Calculate random offset
            int xOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int zOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int yOffset = getRandomOffset(minYRadius, maxYRadius);

            // Get initial coordinates
            int initialX = playerLocation.getBlockX() + xOffset;
            int initialY = playerLocation.getBlockY() + yOffset;
            int initialZ = playerLocation.getBlockZ() + zOffset;

            // For surface only spawning, find the highest block at this X,Z
            if (surfaceOnlySpawning) {
                initialY = world.getHighestBlockYAt(initialX, initialZ);
            }

            // Try to find a safe spawning location
            Location spawnLocation = getSafeLocation(player, initialX, initialY, initialZ);

            if (spawnLocation != null) {
                EntityType typeToSpawn = useRandomMobTypes ? getRandomMobType() : mobType;
                Entity entity = world.spawnEntity(spawnLocation, typeToSpawn);
                markSpawnedMob(entity);
                spawnedEntities.add(entity);
                spawned++;
            }
        }
        return spawnedEntities;
    }

    /**
     * Spawns a group of mobs near a player at a single central location.
     * First finds a safe center point, then spawns mobs clustered around it
     * within the {@link #groupSpacing} radius.
     *
     * @param player The player to spawn the mob group near
     * @return List of successfully spawned entities
     */
    public List<Entity> spawnGroupForPlayer(Player player) {
        List<Entity> spawnedEntities = new ArrayList<>();
        World world = player.getWorld();
        Location playerLocation = player.getLocation();

        // Try to find a suitable location for the group
        Location groupCenter = null;
        int attempts = 0;

        while (groupCenter == null && attempts < maxSpawnAttempts) {
            attempts++;

            // Calculate random offset
            int xOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int zOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
            int yOffset = getRandomOffset(minYRadius, maxYRadius);

            // Get initial coordinates
            int initialX = playerLocation.getBlockX() + xOffset;
            int initialY = playerLocation.getBlockY() + yOffset;
            int initialZ = playerLocation.getBlockZ() + zOffset;

            // For surface only spawning, find the highest block at this X,Z
            if (surfaceOnlySpawning) {
                initialY = world.getHighestBlockYAt(initialX, initialZ);
            }

            // Try to find safe spawning location
            groupCenter = getSafeLocation(player, initialX, initialY, initialZ);
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

            // Get initial coordinates for group member
            int initialX = groupCenter.getBlockX() + xOffset;
            int initialY = groupCenter.getBlockY();
            int initialZ = groupCenter.getBlockZ() + zOffset;

            // For surface only spawning, adjust Y to the highest block
            if (surfaceOnlySpawning) {
                initialY = world.getHighestBlockYAt(initialX, initialZ);
            }

            // Try to find a safe location
            Location spawnLocation = getGroupSafeLocation(player, groupCenter, initialX, initialY, initialZ);

            if (spawnLocation != null) {
                EntityType typeToSpawn = useRandomMobTypes ? getRandomMobType() : mobType;
                Entity entity = world.spawnEntity(spawnLocation, typeToSpawn);
                markSpawnedMob(entity);
                spawnedEntities.add(entity);
                spawned++;
            }
        }

        return spawnedEntities;
    }

    /**
     * Marks an entity as spawned by this event using persistent data.
     * Used to track and manage spawned mobs.
     *
     * @param entity The entity to mark as spawned by this event
     */
    public void markSpawnedMob(Entity entity) {
        entity.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    /**
     * Checks if an entity was spawned by this event.
     *
     * @param entity The entity to check
     * @return true if the entity was spawned by this event, false otherwise
     */
    public boolean isSpawnedMobMarked(Entity entity) {
        return entity.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    /**
     * Removes all mobs that were spawned by this event across all worlds.
     * Adds the event's key to the deletion list and removes marked entities.
     */
    public void killAllSpawnedMob() {
        EventHorizon.entityKeysToDelete.add(key);
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if (isSpawnedMobMarked(entity)) {
                    entity.remove();
                }
            });
        });
    }

    /**
     * Adds a new mob type to the list of possible spawn types.
     * Does nothing if the type is null or already in the list.
     *
     * @param entityType The entity type to add
     * @return This instance for method chaining
     */
    public BaseMobSpawn addMobType(EntityType entityType) {
        if (entityType != null && !this.mobTypes.contains(entityType)) {
            this.mobTypes.add(entityType);
        }
        return this;
    }

    /**
     * Adds multiple mob types to the list of possible spawn types.
     *
     * @param entityTypes List of entity types to add
     * @return This instance for method chaining
     */
    public BaseMobSpawn addMobTypes(List<EntityType> entityTypes) {
        if (entityTypes != null) {
            for (EntityType type : entityTypes) {
                addMobType(type);
            }
        }
        return this;
    }

    /**
     * Removes a mob type from the list of possible spawn types.
     * If the removed type is the current default, resets to a new default.
     *
     * @param entityType The entity type to remove
     * @return This instance for method chaining
     */
    public BaseMobSpawn removeMobType(EntityType entityType) {
        if (entityType != null && this.mobTypes.remove(entityType)) {
            if (this.mobType.equals(entityType)) {
                resetDefaultMobType();
            }
        }
        return this;
    }

    /**
     * Replaces the current list of mob types with a new list.
     * If the new list is null or empty, keeps the default mob type.
     *
     * @param entityTypes New list of entity types to use
     * @return This instance for method chaining
     */
    public BaseMobSpawn setMobTypes(List<EntityType> entityTypes) {
        this.mobTypes.clear();

        if (entityTypes != null && !entityTypes.isEmpty()) {
            this.mobTypes.addAll(entityTypes);
        }

        resetDefaultMobType();
        return this;
    }

    /**
     * Clears all mob types except the current default type.
     * Ensures at least one mob type remains available for spawning.
     *
     * @return This instance for method chaining
     */
    public BaseMobSpawn clearMobTypes() {
        EntityType currentType = this.mobType;
        this.mobTypes.clear();
        this.mobTypes.add(currentType);
        return this;
    }

    /**
     * Resets the default mob type based on the available mob types list.
     * If the list is not empty, sets the first mob type as default.
     * Otherwise, sets ZOMBIE as the default type and adds it to the list.
     */
    private void resetDefaultMobType() {
        if (!this.mobTypes.isEmpty()) {
            this.mobType = this.mobTypes.getFirst();
        } else {
            this.mobType = EntityType.ZOMBIE;
            this.mobTypes.add(EntityType.ZOMBIE);
        }
    }

    /**
     * Gets a random mob type from the available mob types list.
     * Returns a random type only if random mob types are enabled and the list is not empty.
     * Otherwise, returns the default mob type.
     *
     * @return The selected EntityType to spawn
     */
    protected EntityType getRandomMobType() {
        if (useRandomMobTypes && !mobTypes.isEmpty()) {
            return mobTypes.get(random.nextInt(mobTypes.size()));
        }
        return mobType;
    }

    /**
     * Attempts to find a safe location to spawn a mob near the given coordinates.
     * Makes multiple attempts to find a valid location within spawn radius and height limits.
     * Adjusts position based on terrain and safety checks.
     *
     * @param player The player to spawn near
     * @param initialX Initial X coordinate
     * @param initialY Initial Y coordinate
     * @param initialZ Initial Z coordinate
     * @return A safe Location for spawning, or null if none found
     */
    protected Location getSafeLocation(Player player, int initialX, int initialY, int initialZ) {
        World world = player.getWorld();
        int maxTries = maxSpawnAttempts * 3;
        int currentTry = 0;

        int x = initialX;
        int y = initialY;
        int z = initialZ;

        // Check world height boundaries
        if (y < world.getMinHeight()) {
            y = world.getMinHeight();
        } else if (y >= world.getMaxHeight()) {
            y = world.getMaxHeight() - 3;
        }

        while (currentTry < maxTries) {
            Location location = new Location(world, x, y, z);

            if (isSafeLocation(location)) {
                // Center the location in the block
                location.setX(x + 0.5);
                location.setZ(z + 0.5);
                return location;
            }

            // Adjust position based on issues
            Block block = location.getBlock();
            Block blockBelow = location.clone().subtract(0, 1, 0).getBlock();

            if (block.getType().isSolid()) {
                // If current block is solid, move up
                y++;
            } else if (!blockBelow.getType().isSolid() && !isLiquidLocation(blockBelow)) {
                // If there's no solid block below, move down
                y--;
            } else {
                // If other issues, try a small move horizontally
                x += random.nextInt(3) - 1;
                z += random.nextInt(3) - 1;
            }

            // Check if we're still within the radius
            Location playerLocation = player.getLocation();
            double distanceSquared = Math.pow(playerLocation.getX() - x, 2) +
                    Math.pow(playerLocation.getZ() - z, 2);

            if (distanceSquared > Math.pow(maxSpawnRadius, 2)) {
                // If we've moved outside the radius, reset to a new random position within radius
                int xOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
                int zOffset = getRandomOffset(minSpawnRadius, maxSpawnRadius);
                x = playerLocation.getBlockX() + xOffset;
                z = playerLocation.getBlockZ() + zOffset;

                // Reset y based on surface setting
                if (surfaceOnlySpawning) {
                    y = world.getHighestBlockYAt(x, z);
                } else {
                    int yOffset = getRandomOffset(minYRadius, maxYRadius);
                    y = playerLocation.getBlockY() + yOffset;
                }
            }
            currentTry++;
        }
        return null;
    }

    /**
     * Attempts to find a safe location to spawn a mob within a group.
     * Similar to getSafeLocation but maintains proximity to group center.
     *
     * @param player The player to spawn near
     * @param groupCenter The central location of the group
     * @param initialX Initial X coordinate
     * @param initialY Initial Y coordinate
     * @param initialZ Initial Z coordinate
     * @return A safe Location for spawning within group bounds, or null if none found
     */
    protected Location getGroupSafeLocation(Player player, Location groupCenter, int initialX, int initialY, int initialZ) {
        World world = player.getWorld();
        int maxTries = maxSpawnAttempts;
        int currentTry = 0;

        int x = initialX;
        int y = initialY;
        int z = initialZ;

        // Check world height boundaries
        if (y < world.getMinHeight()) {
            y = world.getMinHeight();
        } else if (y >= world.getMaxHeight()) {
            y = world.getMaxHeight() - 3;
        }

        while (currentTry < maxTries) {
            Location location = new Location(world, x, y, z);

            if (isSafeLocation(location)) {
                // Center the location in the block
                location.setX(x + 0.5);
                location.setZ(z + 0.5);
                return location;
            }

            // Adjust position based on issues
            Block block = location.getBlock();
            Block blockBelow = location.clone().subtract(0, 1, 0).getBlock();

            if (block.getType().isSolid()) {
                // If current block is solid, move up
                y++;
            } else if (!blockBelow.getType().isSolid() && !isLiquidLocation(blockBelow)) {
                // If there's no solid block below, move down
                y--;
            } else {
                // If other issues, try a small move horizontally (but stay within group spacing)
                x += random.nextInt(3) - 1;
                z += random.nextInt(3) - 1;
            }

            // Check if we're still within the group spacing radius
            double distanceSquared = Math.pow(groupCenter.getX() - x, 2) + Math.pow(groupCenter.getZ() - z, 2);
            if (distanceSquared > Math.pow(groupSpacing, 2)) {
                // If we've moved outside the group radius, reset to a new position closer to group center
                int xOffset = random.nextInt(groupSpacing * 2 + 1) - groupSpacing;
                int zOffset = random.nextInt(groupSpacing * 2 + 1) - groupSpacing;
                x = groupCenter.getBlockX() + xOffset;
                z = groupCenter.getBlockZ() + zOffset;

                // Reset y based on surface setting
                if (surfaceOnlySpawning) {
                    y = world.getHighestBlockYAt(x, z);
                } else {
                    y = groupCenter.getBlockY() + (random.nextInt(3) - 1); // Small y variance in groups
                }
            }
            currentTry++;
        }

        return null;
    }

    /**
     * Checks if a location is safe for mob spawning.
     * Validates world height bounds, block solidity, and required clearance.
     *
     * @param location The location to check
     * @return true if the location is safe for spawning, false otherwise
     */
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

    /**
     * Checks if a block contains a valid liquid for mob spawning.
     * Considers water and lava based on spawn settings.
     *
     * @param block The block to check
     * @return true if the block contains an allowed liquid, false otherwise
     */
    private boolean isLiquidLocation(Block block) {
        Material type = block.getType();
        return (type == Material.WATER && allowWaterSpawns) || (type == Material.LAVA && allowLavaSpawns);
    }

    /**
     * Checks if a block is safe for mob spawning.
     * A block is considered safe if it's air or an allowed liquid.
     *
     * @param block The block to check
     * @return true if the block is safe for spawning, false otherwise
     */
    private boolean isSafeBlock(Block block) {
        return block.getType() == Material.AIR || isLiquidLocation(block);
    }

    /**
     * Checks if there's sufficient clearance around a location for mob spawning.
     * Validates a rectangular area based on width and height clearance settings.
     *
     * @param location The base location to check clearance around
     * @return true if sufficient clearance exists, false otherwise
     */
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

            int xStart = 0;
            int zStart = 0;

            // If widthClearance is odd, check both negative and positive x and z directions
            if (widthClearance % 2 == 1) {
                xStart = -widthBlocks;
                zStart = -widthBlocks;
            }

            for (int x = xStart; x <= widthBlocks; x++) {
                for (int z = zStart; z <= widthBlocks; z++) {
                    if (!isSafeBlock(world.getBlockAt(baseX + x, checkY, baseZ + z))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Generates a random offset between minimum and maximum values.
     * The returned value will be either positive or negative with equal probability.
     *
     * @param min The minimum absolute value of the offset
     * @param max The maximum absolute value of the offset
     * @return A random integer between -max and -min or between min and max
     */
    private int getRandomOffset(int min, int max) {
        int range = max - min;
        int offset = random.nextInt(range + 1) + min;
        return random.nextBoolean() ? offset : -offset;
    }

    /**
     * Gets the number of mobs spawned in the last execution.
     *
     * @return The count of mobs spawned in the most recent spawn operation
     */
    public int getLastSpawnCount() {
        return lastSpawnCount;
    }

    /**
     * Gets an unmodifiable list of all possible mob types that can be spawned.
     *
     * @return An unmodifiable List of EntityTypes that can be spawned
     */
    public List<EntityType> getMobTypes() {
        return Collections.unmodifiableList(this.mobTypes);
    }

    /**
     * Sets the default mob type to spawn when random mob types are disabled.
     *
     * @param mobType The EntityType to use as default spawn type
     * @return This instance for method chaining
     */
    public BaseMobSpawn setMobType(EntityType mobType) {
        this.mobType = mobType;
        return this;
    }

    /**
     * Sets the number of mobs to spawn per player.
     *
     * @param count The number of mobs to spawn
     * @return This instance for method chaining
     */
    public BaseMobSpawn setMobCount(int count) {
        this.mobCount = count;
        return this;
    }

    /**
     * Sets the maximum radius from the player where mobs can spawn.
     *
     * @param radius The maximum spawn radius in blocks
     * @return This instance for method chaining
     */
    public BaseMobSpawn setMaxSpawnRadius(int radius) {
        this.maxSpawnRadius = radius;
        return this;
    }

    /**
     * Sets the minimum radius from the player where mobs can spawn.
     *
     * @param radius The minimum spawn radius in blocks
     * @return This instance for method chaining
     */
    public BaseMobSpawn setMinSpawnRadius(int radius) {
        this.minSpawnRadius = radius;
        return this;
    }

    /**
     * Sets the maximum vertical distance from the player where mobs can spawn.
     *
     * @param radius The maximum vertical spawn distance in blocks
     * @return This instance for method chaining
     */
    public BaseMobSpawn setMaxYRadius(int radius) {
        this.maxYRadius = radius;
        return this;
    }

    /**
     * Sets the minimum vertical distance from the player where mobs can spawn.
     *
     * @param radius The minimum vertical spawn distance in blocks
     * @return This instance for method chaining
     */
    public BaseMobSpawn setMinYRadius(int radius) {
        this.minYRadius = radius;
        return this;
    }

    /**
     * Sets the maximum number of attempts to find a valid spawn location.
     *
     * @param attempts Maximum number of spawn location attempts per mob
     * @return This instance for method chaining
     */
    public BaseMobSpawn setMaxSpawnAttempts(int attempts) {
        this.maxSpawnAttempts = attempts;
        return this;
    }

    /**
     * Sets the required horizontal clearance for mob spawning.
     *
     * @param clearance The required horizontal clearance in blocks
     * @return This instance for method chaining
     */
    public BaseMobSpawn setWidthClearance(double clearance) {
        this.widthClearance = clearance;
        return this;
    }

    /**
     * Sets the required vertical clearance for mob spawning.
     *
     * @param clearance The required vertical clearance in blocks
     * @return This instance for method chaining
     */
    public BaseMobSpawn setHeightClearance(double clearance) {
        this.heightClearance = clearance;
        return this;
    }

    /**
     * Sets the spacing between mobs when spawning in groups.
     *
     * @param spacing The distance between mobs in blocks
     * @return This instance for method chaining
     */
    public BaseMobSpawn setGroupSpacing(int spacing) {
        this.groupSpacing = spacing;
        return this;
    }

    /**
     * Sets the interval between continuous spawns.
     *
     * @param seconds The time between spawns in seconds
     * @return This instance for method chaining
     */
    public BaseMobSpawn setSpawnInterval(int seconds) {
        this.spawnInterval = seconds;
        return this;
    }

    /**
     * Sets whether mobs should only spawn on surface blocks.
     *
     * @param surfaceOnly True to restrict spawning to surface blocks only
     * @return This instance for method chaining
     */
    public BaseMobSpawn setSurfaceOnlySpawning(boolean surfaceOnly) {
        this.surfaceOnlySpawning = surfaceOnly;
        return this;
    }

    /**
     * Sets whether mobs can spawn in water.
     *
     * @param allow True to allow spawning in water
     * @return This instance for method chaining
     */
    public BaseMobSpawn setAllowWaterSpawns(boolean allow) {
        this.allowWaterSpawns = allow;
        return this;
    }

    /**
     * Sets whether mobs can spawn in lava.
     *
     * @param allow True to allow spawning in lava
     * @return This instance for method chaining
     */
    public BaseMobSpawn setAllowLavaSpawns(boolean allow) {
        this.allowLavaSpawns = allow;
        return this;
    }

    /**
     * Sets whether mobs should spawn in groups or spread out.
     *
     * @param groupSpawn True to enable group spawning
     * @return This instance for method chaining
     */
    public BaseMobSpawn setUseGroupSpawning(boolean groupSpawn) {
        this.useGroupSpawning = groupSpawn;
        return this;
    }

    /**
     * Sets whether mobs should continuously spawn at intervals.
     *
     * @param continuousSpawn True to enable continuous spawning
     * @return This instance for method chaining
     */
    public BaseMobSpawn setUseContinuousSpawning(boolean continuousSpawn) {
        this.useContinuousSpawning = continuousSpawn;
        return this;
    }

    /**
     * Sets whether to randomly select from available mob types.
     *
     * @param useRandom True to enable random mob type selection
     * @return This instance for method chaining
     */
    public BaseMobSpawn setRandomMobTypes(boolean useRandom) {
        this.useRandomMobTypes = useRandom;
        return this;
    }
}