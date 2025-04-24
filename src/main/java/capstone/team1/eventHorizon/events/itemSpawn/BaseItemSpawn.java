package capstone.team1.eventHorizon.events.itemSpawn;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Abstract base class for item spawning events that handles item distribution around players.
 * This class provides configurable spawning behavior for items in the Minecraft world,
 * including options for:
 * <ul>
 *   <li>Single or continuous spawning</li>
 *   <li>Group or spread distribution</li>
 *   <li>Random or fixed item types</li>
 *   <li>Surface-only or 3D space spawning</li>
 *   <li>Liquid (water/lava) spawning permissions</li>
 * </ul>
 * Each spawned item is marked with a unique identifier for tracking and later cleanup.
 * The class supports weighted random selection of items and various safety checks
 * to ensure valid spawn locations.
 *
 * @see BaseEvent
 * @see EventClassification
 */
public abstract class BaseItemSpawn extends BaseEvent {
    /** Plugin instance for server access */
    EventHorizon plugin = EventHorizon.getPlugin();
    /** Random number generator for various random selections */
    protected final Random random = new Random();
    /** Unique identifier for marking spawned items */
    protected final NamespacedKey key;

    /** Default number of items to spawn per event */
    private static final int DEFAULT_ITEM_COUNT = 5;
    /** Default maximum radius from player for item spawning */
    private static final int DEFAULT_MAX_SPAWN_RADIUS = 20;
    /** Default minimum radius from player for item spawning */
    private static final int DEFAULT_MIN_SPAWN_RADIUS = 3;
    /** Default maximum vertical radius for item spawning */
    private static final int DEFAULT_MAX_Y_RADIUS = 20;
    /** Default minimum vertical radius for item spawning */
    private static final int DEFAULT_MIN_Y_RADIUS = 3;
    /** Default maximum attempts to find a valid spawn location */
    private static final int DEFAULT_MAX_SPAWN_ATTEMPTS = 20;
    /** Default interval between continuous spawns in seconds */
    private static final int DEFAULT_SPAWN_INTERVAL = 60;
    /** Default horizontal clearance required for spawn location */
    private static final double DEFAULT_WIDTH_CLEARANCE = 1;
    /** Default vertical clearance required for spawn location */
    private static final double DEFAULT_HEIGHT_CLEARANCE = 1;
    /** Default spacing between items when group spawning */
    private static final int DEFAULT_GROUP_SPACING = 3;
    /** Default setting for surface-only spawning */
    private static final boolean DEFAULT_SURFACE_ONLY_SPAWNING = false;
    /** Default setting for allowing spawns in water */
    private static final boolean DEFAULT_ALLOW_WATER_SPAWNS = false;
    /** Default setting for allowing spawns in lava */
    private static final boolean DEFAULT_ALLOW_LAVA_SPAWNS = false;
    /** Default setting for group spawning mode */
    private static final boolean DEFAULT_USE_GROUP_SPAWNING = false;
    /** Default setting for continuous spawning mode */
    private static final boolean DEFAULT_USE_CONTINUOUS_SPAWNING = false;
    /** Default setting for random item type selection */
    private static final boolean DEFAULT_USE_RANDOM_ITEM_TYPES = false;

    /** The default item type to spawn */
    protected ItemStack itemType = new ItemStack(Material.STONE);
    /** List of items with their spawn weights for random selection */
    protected List<Pair<ItemStack, Double>> weightedItems = new ArrayList<>();
    /** Number of items to spawn per event */
    protected int itemCount = DEFAULT_ITEM_COUNT;
    /** Maximum radius from player for item spawning */
    protected int maxSpawnRadius = DEFAULT_MAX_SPAWN_RADIUS;
    /** Minimum radius from player for item spawning */
    protected int minSpawnRadius = DEFAULT_MIN_SPAWN_RADIUS;
    /** Maximum vertical radius for item spawning */
    protected int maxYRadius = DEFAULT_MAX_Y_RADIUS;
    /** Minimum vertical radius for item spawning */
    protected int minYRadius = DEFAULT_MIN_Y_RADIUS;
    /** Maximum attempts to find a valid spawn location */
    protected int maxSpawnAttempts = DEFAULT_MAX_SPAWN_ATTEMPTS;
    /** Required horizontal clearance for spawn location */
    protected double widthClearance = DEFAULT_WIDTH_CLEARANCE;
    /** Required vertical clearance for spawn location */
    protected double heightClearance = DEFAULT_HEIGHT_CLEARANCE;
    /** Spacing between items when group spawning */
    protected int groupSpacing = DEFAULT_GROUP_SPACING;
    /** Count of items spawned in the last execution */
    private int lastSpawnCount = 0;

    /** Whether items should only spawn on surface blocks */
    protected boolean surfaceOnlySpawning = DEFAULT_SURFACE_ONLY_SPAWNING;
    /** Whether items can spawn in water */
    protected boolean allowWaterSpawns = DEFAULT_ALLOW_WATER_SPAWNS;
    /** Whether items can spawn in lava */
    protected boolean allowLavaSpawns = DEFAULT_ALLOW_LAVA_SPAWNS;
    /** Whether items should spawn in groups */
    protected boolean useGroupSpawning = DEFAULT_USE_GROUP_SPAWNING;
    /** Whether spawning should occur continuously */
    protected boolean useContinuousSpawning = DEFAULT_USE_CONTINUOUS_SPAWNING;
    /** Whether to randomly select item types from weightedItems */
    protected boolean useRandomItemTypes = DEFAULT_USE_RANDOM_ITEM_TYPES;

    /** Task for continuous spawning mode */
    protected BukkitTask continuousTask = null;
    /** Interval between continuous spawns in seconds */
    protected int spawnInterval = DEFAULT_SPAWN_INTERVAL;

    // Constructors
    /**
     * Constructs a basic item spawn event with default item type.
     *
     * @param classification the event classification (NEUTRAL, HOSTILE, etc.)
     * @param eventName unique identifier for this event
     */
    public BaseItemSpawn(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);
    }
    /**
     * Constructs an item spawn event with a specified default item type.
     *
     * @param defaultItemType the ItemStack to be spawned
     * @param eventName unique identifier for this event
     */
    public BaseItemSpawn(ItemStack defaultItemType, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.itemType = defaultItemType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }
    /**
     * Constructs an item spawn event with specified item type and classification.
     *
     * @param defaultItemType the ItemStack to be spawned
     * @param classification the event classification (NEUTRAL, HOSTILE, etc.)
     * @param eventName unique identifier for this event
     */
    public BaseItemSpawn(ItemStack defaultItemType, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.itemType = defaultItemType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }
    /**
     * Constructs an item spawn event with weighted random item selection.
     *
     * @param weightedItems list of item-weight pairs for random selection
     * @param classification the event classification (NEUTRAL, HOSTILE, etc.)
     * @param eventName unique identifier for this event
     */
    public BaseItemSpawn(List<Pair<ItemStack, Double>> weightedItems, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.weightedItems.addAll(weightedItems);
        this.useRandomItemTypes = true;
        this.key = new NamespacedKey(plugin, this.eventName);

        // Set default item type to first in list
        if (!weightedItems.isEmpty()) {
            this.itemType = weightedItems.getFirst().getLeft();
        } else {
            this.weightedItems.add(Pair.of(new ItemStack(Material.STONE), 1.0));
        }
    }

    /**
     * Executes the item spawn event.
     * Either starts continuous spawning or performs a one-time spawn for all players.
     */
    @Override
    public void execute() {
        try {
            this.lastSpawnCount = 0;

            if (useContinuousSpawning) {
                // Start continuous task for ongoing spawning
                boolean started = startContinuousTask();
                if (started) {
                    if (useRandomItemTypes) {
                        MsgUtility.log("Event " + eventName +
                                " started continuous spawning of random items" +
                                " with interval of " + spawnInterval + " seconds");
                    } else {
                        MsgUtility.log("Event " + eventName +
                                " started continuous spawning of " + itemType.toString() +
                                " items with interval of " + spawnInterval + " seconds");
                    }
                } else {
                    MsgUtility.log("Event " + eventName +
                            " tried to start continuous spawning but it was already running");
                }
            } else {
                // Do a one-time spawn for all players
                int spawned = spawnForAllPlayers();
                this.lastSpawnCount = spawned;

                if (useRandomItemTypes) {
                    MsgUtility.log("Event " + eventName +
                            " spawned " + spawned + " random items across " +
                            plugin.getServer().getOnlinePlayers().size() +
                            " players");
                } else {
                    MsgUtility.log("Event " + eventName +
                            " spawned " + spawned + " " + itemType.toString() +
                            " items across " + plugin.getServer().getOnlinePlayers().size() +
                            " players");
                }
            }
        } catch (Exception e) {
            MsgUtility.warning("Error spawning items in " + eventName + ": " + e.getMessage());
        }
    }

    /**
     * Terminates the event by stopping any continuous spawning tasks.
     */
    @Override
    public void terminate() {
        boolean stopped = stopContinuousTask();

        if (stopped) {
            if (useRandomItemTypes) {
                MsgUtility.log("Event " + eventName + " stopped continuous spawning of random items");
            } else {
                MsgUtility.log("Event " + eventName + " stopped continuous spawning of " + itemType.toString() + " items");
            }
        } else {
            MsgUtility.warning("Event " + eventName + " tried to stop continuous spawning but it was already stopped");
        }
    }

    /**
     * Starts a continuous spawning task if one is not already running.
     *
     * @return true if task was started, false if already running
     */
    public boolean startContinuousTask() {
        // Check if task is already running
        if (continuousTask != null && !continuousTask.isCancelled()) {
            return false;
        }

        // Use BukkitRunnable for continuous task that spawns items for all players
        continuousTask = new BukkitRunnable() {
            @Override
            public void run() {
                spawnForAllPlayers();
            }
        }.runTaskTimer(plugin, 20L, spawnInterval * 20L);

        return true;
    }

    /**
     * Stops the current continuous spawning task if one is running.
     *
     * @return true if task was stopped, false if no task was running
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
     * Hook method called when an item is spawned.
     * Can be overridden by child classes for custom behavior.
     *
     * @param item the spawned item entity
     * @param player the player the item was spawned for
     */
    protected void onItemSpawned(Item item, Player player) {
    }

    /**
     * Spawns items for all online players.
     *
     * @return total number of items spawned across all players
     */
    public int spawnForAllPlayers() {
        int totalSpawned = 0;
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        for (Player player : players) {
            List<Item> spawnedItems = spawnForPlayer(player);
            int playerSpawnCount = spawnedItems.size();
            totalSpawned += playerSpawnCount;

            if (useRandomItemTypes) {
                MsgUtility.log("Spawned " + playerSpawnCount + " random items for player " + player.getName());
            } else {
                MsgUtility.log("Spawned " + playerSpawnCount + " " + itemType.toString() +
                        " items for player " + player.getName());
            }

            // Optional hook for child classes to implement additional logic
            for (Item item : spawnedItems) {
                onItemSpawned(item, player);
            }
        }
        return totalSpawned;
    }

    /**
     * Spawns items for a specific player.
     *
     * @param player the player to spawn items for
     * @return list of spawned item entities
     */
    public List<Item> spawnForPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }
        return useGroupSpawning ? spawnGroupForPlayer(player) : spawnSpreadForPlayer(player);
    }

    /**
     * Spawns items spread around a player at random locations.
     *
     * @param player the player to spawn items around
     * @return list of spawned item entities
     */
    public List<Item> spawnSpreadForPlayer(Player player) {
        List<Item> spawnedItems = new ArrayList<>();
        World world = player.getWorld();
        Location playerLocation = player.getLocation();

        int attempts = 0;
        int spawned = 0;

        while (spawned < itemCount && attempts < maxSpawnAttempts) {
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
                ItemStack itemToSpawn = useRandomItemTypes ? getRandomWeightedItem() : itemType;
                Item item = world.dropItem(spawnLocation, itemToSpawn);
                markSpawnedItem(item);
                spawnedItems.add(item);
                spawned++;
            }
        }
        return spawnedItems;
    }

    /**
     * Spawns a group of items near a player at a single central location.
     *
     * @param player the player to spawn items around
     * @return list of spawned item entities
     */
    public List<Item> spawnGroupForPlayer(Player player) {
        List<Item> spawnedItems = new ArrayList<>();
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
            return spawnedItems;
        }

        // Spawn the items in a group around the center
        int spawned = 0;
        attempts = 0;

        while (spawned < itemCount && attempts < maxSpawnAttempts * 2) {
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
                ItemStack itemToSpawn = useRandomItemTypes ? getRandomWeightedItem() : itemType;
                Item item = world.dropItem(spawnLocation, itemToSpawn);
                markSpawnedItem(item);
                spawnedItems.add(item);
                spawned++;
            }
        }

        return spawnedItems;
    }

    /**
     * Marks an item entity with this event's unique identifier.
     *
     * @param item the item entity to mark
     */
    public void markSpawnedItem(Item item) {
        item.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    /**
     * Checks if an item entity was marked by this event.
     *
     * @param item the item entity to check
     * @return true if the item was marked by this event
     */
    public boolean isSpawnedItemMarked(Item item) {
        return item.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    /**
     * Deletes all items that were marked by this event across all worlds.
     */
    public void deleteSpawnedItems() {
        EventHorizon.entityKeysToDelete.add(key);
        Bukkit.getWorlds().forEach(world -> {
            world.getEntitiesByClass(Item.class).forEach(item -> {
                if (isSpawnedItemMarked(item)) {
                    item.remove();
                }
            });
        });
    }

    /**
     * Adds multiple weighted items to the random selection pool.
     *
     * @param items list of item-weight pairs to add
     * @return this instance for method chaining
     */
    public BaseItemSpawn addWeightedItems(List<Pair<ItemStack, Double>> items) {
        if (items != null) {
            weightedItems.addAll(items);
        }
        return this;
    }

    /**
     * Removes a specific item type from the weighted random selection pool.
     *
     * @param itemToRemove the item type to remove
     * @return this instance for method chaining
     */
    public BaseItemSpawn removeWeightedItem(ItemStack itemToRemove) {
        if (itemToRemove != null) {
            weightedItems.removeIf(pair -> pair.getLeft().isSimilar(itemToRemove));
        }
        return this;
    }

    /**
     * Sets the complete list of weighted items for random selection.
     * Replaces any existing weighted items.
     *
     * @param items list of item-weight pairs
     * @return this instance for method chaining
     */
    public BaseItemSpawn setWeightedItems(List<Pair<ItemStack, Double>> items) {
        // Clear existing items
        weightedItems.clear();

        // Add new ones if not null
        if (items != null) {
            weightedItems.addAll(items);
        }

        return this;
    }

    /**
     * Selects a random item from the weighted item pool.
     *
     * @return randomly selected ItemStack based on weights
     */
    protected ItemStack getRandomWeightedItem() {
        if (!useRandomItemTypes || weightedItems.isEmpty()) {
            return itemType;
        }

        double totalWeight = weightedItems.stream()
                .mapToDouble(Pair::getRight)
                .sum();

        double randomValue = ThreadLocalRandom.current().nextDouble(totalWeight);
        double cumulativeWeight = 0;

        for (Pair<ItemStack, Double> item : weightedItems) {
            cumulativeWeight += item.getRight();
            if (randomValue <= cumulativeWeight) {
                return item.getLeft();
            }
        }

        return weightedItems.getLast().getLeft();
    }

    /**
     * Finds a safe location to spawn an item near the given coordinates.
     *
     * @param player the player reference for spawn radius
     * @param initialX starting X coordinate
     * @param initialY starting Y coordinate
     * @param initialZ starting Z coordinate
     * @return safe location or null if none found
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
                location.setY(y + 0.5);
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
     * Finds a safe location to spawn an item within a group.
     *
     * @param player the player reference
     * @param groupCenter the center location of the group
     * @param initialX starting X coordinate
     * @param initialY starting Y coordinate
     * @param initialZ starting Z coordinate
     * @return safe location or null if none found
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
                location.setY(y + 0.5);
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
     * Checks if a location is safe for item spawning.
     *
     * @param location the location to check
     * @return true if the location is safe for spawning
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
     * Checks if a block contains a permitted liquid.
     *
     * @param block the block to check
     * @return true if the block contains an allowed liquid
     */
    private boolean isLiquidLocation(Block block) {
        Material type = block.getType();
        return (type == Material.WATER && allowWaterSpawns) || (type == Material.LAVA && allowLavaSpawns);
    }

    /**
     * Checks if a block is safe for item spawning.
     *
     * @param block the block to check
     * @return true if the block is safe
     */
    private boolean isSafeBlock(Block block) {
        return block.getType() == Material.AIR || isLiquidLocation(block);
    }

    /**
     * Checks if there is enough clearance around a location.
     *
     * @param location the location to check
     * @return true if there is sufficient clearance
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
     * Generates a random offset between min and max values.
     *
     * @param min minimum value
     * @param max maximum value
     * @return random offset between -max and -min or min and max
     */
    private int getRandomOffset(int min, int max) {
        int range = max - min;
        int offset = random.nextInt(range + 1) + min;
        return random.nextBoolean() ? offset : -offset;
    }

    /**
     * Gets the number of items spawned in the last execution.
     *
     * @return the count of items spawned in the most recent spawn operation
     */
    public int getLastSpawnCount() {
        return lastSpawnCount;
    }

    /**
     * Sets the default item type to spawn when random item types are disabled.
     *
     * @param itemType the ItemStack to use as default spawn type
     * @return this instance for method chaining
     */
    public BaseItemSpawn setItemType(ItemStack itemType) {
        this.itemType = itemType;
        return this;
    }

    /**
     * Sets the number of items to spawn per player.
     *
     * @param count the number of items to spawn
     * @return this instance for method chaining
     */
    public BaseItemSpawn setItemCount(int count) {
        this.itemCount = count;
        return this;
    }

    /**
     * Sets the maximum radius from the player where items can spawn.
     *
     * @param radius the maximum spawn radius in blocks
     * @return this instance for method chaining
     */
    public BaseItemSpawn setMaxSpawnRadius(int radius) {
        this.maxSpawnRadius = radius;
        return this;
    }

    /**
     * Sets the minimum radius from the player where items can spawn.
     *
     * @param radius the minimum spawn radius in blocks
     * @return this instance for method chaining
     */
    public BaseItemSpawn setMinSpawnRadius(int radius) {
        this.minSpawnRadius = radius;
        return this;
    }

    /**
     * Sets the maximum vertical distance from the player where items can spawn.
     *
     * @param radius the maximum vertical spawn distance in blocks
     * @return this instance for method chaining
     */
    public BaseItemSpawn setMaxYRadius(int radius) {
        this.maxYRadius = radius;
        return this;
    }

    /**
     * Sets the minimum vertical distance from the player where items can spawn.
     *
     * @param radius the minimum vertical spawn distance in blocks
     * @return this instance for method chaining
     */
    public BaseItemSpawn setMinYRadius(int radius) {
        this.minYRadius = radius;
        return this;
    }

    /**
     * Sets the maximum number of attempts to find a valid spawn location.
     *
     * @param attempts maximum number of spawn location attempts per item
     * @return this instance for method chaining
     */
    public BaseItemSpawn setMaxSpawnAttempts(int attempts) {
        this.maxSpawnAttempts = attempts;
        return this;
    }

    /**
     * Sets the required horizontal clearance for item spawning.
     *
     * @param clearance the required horizontal clearance in blocks
     * @return this instance for method chaining
     */
    public BaseItemSpawn setWidthClearance(double clearance) {
        this.widthClearance = clearance;
        return this;
    }

    /**
     * Sets the required vertical clearance for item spawning.
     *
     * @param clearance the required vertical clearance in blocks
     * @return this instance for method chaining
     */
    public BaseItemSpawn setHeightClearance(double clearance) {
        this.heightClearance = clearance;
        return this;
    }

    /**
     * Sets the spacing between items when spawning in groups.
     *
     * @param spacing the distance between items in blocks
     * @return this instance for method chaining
     */
    public BaseItemSpawn setGroupSpacing(int spacing) {
        this.groupSpacing = spacing;
        return this;
    }

    /**
     * Sets the interval between continuous spawns.
     *
     * @param seconds the time between spawns in seconds
     * @return this instance for method chaining
     */
    public BaseItemSpawn setSpawnInterval(int seconds) {
        this.spawnInterval = seconds;
        return this;
    }

    /**
     * Sets whether items should only spawn on surface blocks.
     *
     * @param surfaceOnly true to restrict spawning to surface blocks only
     * @return this instance for method chaining
     */
    public BaseItemSpawn setSurfaceOnlySpawning(boolean surfaceOnly) {
        this.surfaceOnlySpawning = surfaceOnly;
        return this;
    }

    /**
     * Sets whether items can spawn in water.
     *
     * @param allow true to allow spawning in water
     * @return this instance for method chaining
     */
    public BaseItemSpawn setAllowWaterSpawns(boolean allow) {
        this.allowWaterSpawns = allow;
        return this;
    }

    /**
     * Sets whether items can spawn in lava.
     *
     * @param allow true to allow spawning in lava
     * @return this instance for method chaining
     */
    public BaseItemSpawn setAllowLavaSpawns(boolean allow) {
        this.allowLavaSpawns = allow;
        return this;
    }

    /**
     * Sets whether items should spawn in groups or spread out.
     *
     * @param groupSpawn true to enable group spawning
     * @return this instance for method chaining
     */
    public BaseItemSpawn setUseGroupSpawning(boolean groupSpawn) {
        this.useGroupSpawning = groupSpawn;
        return this;
    }

    /**
     * Sets whether items should continuously spawn at intervals.
     *
     * @param continuousSpawn true to enable continuous spawning
     * @return this instance for method chaining
     */
    public BaseItemSpawn setUseContinuousSpawning(boolean continuousSpawn) {
        this.useContinuousSpawning = continuousSpawn;
        return this;
    }

    /**
     * Sets whether to randomly select from available item types.
     *
     * @param useRandom true to enable random item type selection
     * @return this instance for method chaining
     */
    public BaseItemSpawn setRandomItemTypes(boolean useRandom) {
        this.useRandomItemTypes = useRandom;
        return this;
    }
}
