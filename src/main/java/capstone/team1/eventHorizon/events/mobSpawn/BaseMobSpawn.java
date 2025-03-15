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

import static capstone.team1.eventHorizon.utility.MsgUtil.log;
import static capstone.team1.eventHorizon.utility.MsgUtil.warning;

public abstract class BaseMobSpawn extends BaseEvent {
    protected final Plugin plugin;
    protected final Random random = new Random();
    protected final NamespacedKey key;

    // Default configuration values
    private static final int DEFAULT_MOB_COUNT = 5;
    private static final int DEFAULT_MAX_SPAWN_RADIUS = 20;
    private static final int DEFAULT_MIN_SPAWN_RADIUS = 3;
    private static final int DEFAULT_MAX_Y_RADIUS = 20;
    private static final int DEFAULT_MIN_Y_RADIUS = 3;
    private static final int DEFAULT_MAX_SPAWN_ATTEMPTS = 20;
    private static final double DEFAULT_WIDTH_CLEARANCE = 1;
    private static final double DEFAULT_HEIGHT_CLEARANCE = 2;
    private static final int DEFAULT_GROUP_SPACING = 3;
    private static final int DEFAULT_SPAWN_INTERVAL = 60; // Seconds

    // Entity properties
    public EntityType mobType = EntityType.ZOMBIE;
    public int mobCount = DEFAULT_MOB_COUNT;
    public int maxSpawnRadius = DEFAULT_MAX_SPAWN_RADIUS;
    public int minSpawnRadius = DEFAULT_MIN_SPAWN_RADIUS;
    public int maxYRadius = DEFAULT_MAX_Y_RADIUS;
    public int minYRadius = DEFAULT_MIN_Y_RADIUS;
    public int maxSpawnAttempts = DEFAULT_MAX_SPAWN_ATTEMPTS;
    public double widthClearance = DEFAULT_WIDTH_CLEARANCE;
    public double heightClearance = DEFAULT_HEIGHT_CLEARANCE;
    public int groupSpacing = DEFAULT_GROUP_SPACING;
    private int lastSpawnCount = 0;

    // Flags
    public boolean surfaceOnlySpawning = false;
    public boolean allowWaterSpawns = false;
    public boolean allowLavaSpawns = false;
    public boolean useGroupSpawning = false;
    public boolean useContinuousSpawning = false;

    // Task management
    public BukkitTask continuousTask = null;
    public int spawnInterval = DEFAULT_SPAWN_INTERVAL;

    // Constructors
    public BaseMobSpawn(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseMobSpawn(EntityType defaultMobType, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.mobType = defaultMobType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseMobSpawn(EntityType defaultMobType, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.mobType = defaultMobType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    // Executes the event
    @Override
    public void execute() {
        try {
            this.lastSpawnCount = 0;

            if (useContinuousSpawning) {
                // Start continuous task for ongoing spawning
                boolean started = startContinuousTask();
                if (started) {
                    log("Event " + eventName +
                            " started continuous spawning of " + mobType.toString() +
                            " mobs with interval of " + spawnInterval + " seconds");
                } else {
                    log("Event " + eventName +
                            " tried to start continuous spawning but it was already running");
                }
            } else {
                // Do a one-time spawn for all players
                int spawned = spawnForAllPlayers();
                this.lastSpawnCount = spawned;

                log("Event " + eventName +
                        " spawned " + spawned + " " + mobType.toString() +
                        " mobs across " + plugin.getServer().getOnlinePlayers().size() +
                        " players");
            }
        } catch (Exception e) {
            warning("Error spawning mobs in " + eventName + ": " + e.getMessage());
        }
    }

    //  stops the event
    @Override
    public void terminate() {
        boolean stopped = stopContinuousTask();

        if (stopped) {
            log("Event " + eventName + " stopped continuous spawning of " + mobType.toString() + " mobs");
        } else {
            warning("Event " + eventName + " tried to stop continuous spawning but it was already stopped");
        }
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

    // Called when a mob is spawned
    protected void onMobSpawned(Entity entity, Player player) {
    }

    // Calls spawning methods for all online players
    public int spawnForAllPlayers() {
        int totalSpawned = 0;
//        log("Attempting to spawn mobs for " + plugin.getServer().getOnlinePlayers().size() + " players");

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            List<Entity> spawnedEntities = spawnForPlayer(player);
            int playerSpawnCount = spawnedEntities.size();
            totalSpawned += playerSpawnCount;

            log("Spawned " + playerSpawnCount + " " + mobType.toString() +
                    " for player " + player.getName());

            for (Entity entity : spawnedEntities) {
                onMobSpawned(entity, player);
            }
        }
        return totalSpawned;
    }

    public List<Entity> spawnForPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }
        return useGroupSpawning ? spawnGroupForPlayer(player) : spawnSpreadForPlayer(player);
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
                Entity entity = world.spawnEntity(spawnLocation, mobType);
                markSpawnedMob(entity);
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
                Entity entity = world.spawnEntity(spawnLocation, mobType);
                markSpawnedMob(entity);
                spawnedEntities.add(entity);
                spawned++;
            }
        }

        return spawnedEntities;
    }

    // Marks a spawned mob as marked
    public void markSpawnedMob(Entity entity) {
        entity.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    // Checks if a spawned mob is marked
    public boolean isSpawnedMobMarked(Entity entity) {
        return entity.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    // Kills all spawned marked mobs
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

    // Gets a safe location for a spawned mob
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

    // Gets a safe location for a group of spawned mobs
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

        for (int y = 0; y <= heightBlocks; y++) {
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

    // Gets a random offset between min and max
    private int getRandomOffset(int min, int max) {
        int range = max - min;
        int offset = random.nextInt(range + 1) + min;
        return random.nextBoolean() ? offset : -offset;
    }

    // Getters
    public int getLastSpawnCount() {
        return lastSpawnCount;
    }

    // Setters
    public BaseMobSpawn setMobType(EntityType mobType) {
        this.mobType = mobType;
        return this;
    }

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

    public BaseMobSpawn setMaxYRadius(int radius) {
        this.maxYRadius = radius;
        return this;
    }

    public BaseMobSpawn setMinYRadius(int radius) {
        this.minYRadius = radius;
        return this;
    }

    public BaseMobSpawn setMaxSpawnAttempts(int attempts) {
        this.maxSpawnAttempts = attempts;
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

    public BaseMobSpawn setSpawnInterval(int seconds) {
        this.spawnInterval = seconds;
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

    public BaseMobSpawn setUseGroupSpawning(boolean groupSpawn) {
        this.useGroupSpawning = groupSpawn;
        return this;
    }

    public BaseMobSpawn setUseContinuousSpawning(boolean continuousSpawn) {
        this.useContinuousSpawning = continuousSpawn;
        return this;
    }
}