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

public abstract class BaseItemSpawn extends BaseEvent {
    EventHorizon plugin = EventHorizon.getPlugin();
    protected final Random random = new Random();
    protected final NamespacedKey key;

    // Default configuration values
    private static final int DEFAULT_ITEM_COUNT = 5;
    private static final int DEFAULT_MAX_SPAWN_RADIUS = 20;
    private static final int DEFAULT_MIN_SPAWN_RADIUS = 3;
    private static final int DEFAULT_MAX_Y_RADIUS = 20;
    private static final int DEFAULT_MIN_Y_RADIUS = 3;
    private static final int DEFAULT_MAX_SPAWN_ATTEMPTS = 20;
    private static final int DEFAULT_SPAWN_INTERVAL = 60; // Seconds
    private static final double DEFAULT_WIDTH_CLEARANCE = 1;
    private static final double DEFAULT_HEIGHT_CLEARANCE = 1;
    private static final int DEFAULT_GROUP_SPACING = 3;
    private static final boolean DEFAULT_SURFACE_ONLY_SPAWNING = false;
    private static final boolean DEFAULT_ALLOW_WATER_SPAWNS = false;
    private static final boolean DEFAULT_ALLOW_LAVA_SPAWNS = false;
    private static final boolean DEFAULT_USE_GROUP_SPAWNING = false;
    private static final boolean DEFAULT_USE_CONTINUOUS_SPAWNING = false;
    private static final boolean DEFAULT_USE_RANDOM_ITEM_TYPES = false;

    // Item properties
    protected ItemStack itemType = new ItemStack(Material.STONE);
    protected List<Pair<ItemStack, Double>> weightedItems = new ArrayList<>();
    protected int itemCount = DEFAULT_ITEM_COUNT;
    protected int maxSpawnRadius = DEFAULT_MAX_SPAWN_RADIUS;
    protected int minSpawnRadius = DEFAULT_MIN_SPAWN_RADIUS;
    protected int maxYRadius = DEFAULT_MAX_Y_RADIUS;
    protected int minYRadius = DEFAULT_MIN_Y_RADIUS;
    protected int maxSpawnAttempts = DEFAULT_MAX_SPAWN_ATTEMPTS;
    protected double widthClearance = DEFAULT_WIDTH_CLEARANCE;
    protected double heightClearance = DEFAULT_HEIGHT_CLEARANCE;
    protected int groupSpacing = DEFAULT_GROUP_SPACING;
    private int lastSpawnCount = 0;

    // Flags
    protected boolean surfaceOnlySpawning = DEFAULT_SURFACE_ONLY_SPAWNING;
    protected boolean allowWaterSpawns = DEFAULT_ALLOW_WATER_SPAWNS;
    protected boolean allowLavaSpawns = DEFAULT_ALLOW_LAVA_SPAWNS;
    protected boolean useGroupSpawning = DEFAULT_USE_GROUP_SPAWNING;
    protected boolean useContinuousSpawning = DEFAULT_USE_CONTINUOUS_SPAWNING;
    protected boolean useRandomItemTypes = DEFAULT_USE_RANDOM_ITEM_TYPES;

    // Task management
    protected BukkitTask continuousTask = null;
    protected int spawnInterval = DEFAULT_SPAWN_INTERVAL;

    // Constructors
    public BaseItemSpawn(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseItemSpawn(ItemStack defaultItemType, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.itemType = defaultItemType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseItemSpawn(ItemStack defaultItemType, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.itemType = defaultItemType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

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

    // Executes the event
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

    // Terminates the event
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

    // Starts continuous task for ongoing spawning
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

    // Stops continuous task
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

    // Called when an item is spawned (optional override for child classes)
    protected void onItemSpawned(Item item, Player player) {
    }

    // Calls spawning methods for all online players
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

    // Spawns items for a specific player
    public List<Item> spawnForPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }
        return useGroupSpawning ? spawnGroupForPlayer(player) : spawnSpreadForPlayer(player);
    }

    // Spawns items spread around the player
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

    // Spawns a group of items near the player
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

    // Marks a spawned item as marked
    public void markSpawnedItem(Item item) {
        item.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    // Checks if a spawned item is marked
    public boolean isSpawnedItemMarked(Item item) {
        return item.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    // Deletes all spawned marked items
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

    // Method to add multiple weighted items at once
    public BaseItemSpawn addWeightedItems(List<Pair<ItemStack, Double>> items) {
        if (items != null) {
            weightedItems.addAll(items);
        }
        return this;
    }

    // Removes a weighted item for random item spawning
    public BaseItemSpawn removeWeightedItem(ItemStack itemToRemove) {
        if (itemToRemove != null) {
            weightedItems.removeIf(pair -> pair.getLeft().isSimilar(itemToRemove));
        }
        return this;
    }

    // Method to set the entire list of weighted items
    public BaseItemSpawn setWeightedItems(List<Pair<ItemStack, Double>> items) {
        // Clear existing items
        weightedItems.clear();

        // Add new ones if not null
        if (items != null) {
            weightedItems.addAll(items);
        }

        return this;
    }

    // Method to get random weighted item
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

    // Gets a safe location for a spawned item
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

    // Gets a safe location for a group of spawned items
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

    // Getter for last spawn count
    public int getLastSpawnCount() {
        return lastSpawnCount;
    }

    // Setters for configuration
    public BaseItemSpawn setItemType(ItemStack itemType) {
        this.itemType = itemType;
        return this;
    }

    public BaseItemSpawn setItemCount(int count) {
        this.itemCount = count;
        return this;
    }

    public BaseItemSpawn setMaxSpawnRadius(int radius) {
        this.maxSpawnRadius = radius;
        return this;
    }

    public BaseItemSpawn setMinSpawnRadius(int radius) {
        this.minSpawnRadius = radius;
        return this;
    }

    public BaseItemSpawn setMaxYRadius(int radius) {
        this.maxYRadius = radius;
        return this;
    }

    public BaseItemSpawn setMinYRadius(int radius) {
        this.minYRadius = radius;
        return this;
    }

    public BaseItemSpawn setMaxSpawnAttempts(int attempts) {
        this.maxSpawnAttempts = attempts;
        return this;
    }

    public BaseItemSpawn setWidthClearance(double clearance) {
        this.widthClearance = clearance;
        return this;
    }

    public BaseItemSpawn setHeightClearance(double clearance) {
        this.heightClearance = clearance;
        return this;
    }

    public BaseItemSpawn setGroupSpacing(int spacing) {
        this.groupSpacing = spacing;
        return this;
    }

    public BaseItemSpawn setSpawnInterval(int seconds) {
        this.spawnInterval = seconds;
        return this;
    }

    public BaseItemSpawn setSurfaceOnlySpawning(boolean surfaceOnly) {
        this.surfaceOnlySpawning = surfaceOnly;
        return this;
    }

    public BaseItemSpawn setAllowWaterSpawns(boolean allow) {
        this.allowWaterSpawns = allow;
        return this;
    }

    public BaseItemSpawn setAllowLavaSpawns(boolean allow) {
        this.allowLavaSpawns = allow;
        return this;
    }

    public BaseItemSpawn setUseGroupSpawning(boolean groupSpawn) {
        this.useGroupSpawning = groupSpawn;
        return this;
    }

    public BaseItemSpawn setUseContinuousSpawning(boolean continuousSpawn) {
        this.useContinuousSpawning = continuousSpawn;
        return this;
    }

    public BaseItemSpawn setRandomItemTypes(boolean useRandom) {
        this.useRandomItemTypes = useRandom;
        return this;
    }
}
