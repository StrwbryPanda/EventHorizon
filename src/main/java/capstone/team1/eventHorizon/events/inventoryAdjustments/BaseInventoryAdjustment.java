package capstone.team1.eventHorizon.events.inventoryAdjustments;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.utility.PlayerInventoryListener;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseInventoryAdjustment extends BaseEvent {
    EventHorizon plugin = EventHorizon.getPlugin();
    protected final Random random = new Random();
    protected final NamespacedKey key;

    // Default configuration values
    private static final int DEFAULT_OPERATION_INTERVAL = 60; // Seconds
    private static final boolean DEFAULT_USE_CONTINUOUS_OPERATION = false;

    // Item properties
    protected List<Pair<ItemStack, Double>> weightedItems = new ArrayList<>();
    protected Map<EquipmentSlot, ItemStack> equipmentItems = new HashMap<>();
    protected ItemStack itemType = new ItemStack(Material.STONE);

    // Flags
    protected boolean useContinuousOperation = DEFAULT_USE_CONTINUOUS_OPERATION;

    // Task management
    protected BukkitTask continuousTask = null;
    protected int operationInterval = DEFAULT_OPERATION_INTERVAL;
    private int lastOperationCount = 0;

    // Constructors
    public BaseInventoryAdjustment(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseInventoryAdjustment(ItemStack defaultItemType, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.itemType = defaultItemType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseInventoryAdjustment(List<Pair<ItemStack, Double>> weightedItems, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);
        this.weightedItems.addAll(weightedItems);
    }

    public BaseInventoryAdjustment(Map<EquipmentSlot, ItemStack> equipmentItems, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);
        this.equipmentItems.putAll(equipmentItems);
    }

    // Executes the event
    @Override
    public void execute() {
        try {
            this.lastOperationCount = 0;

            if (useContinuousOperation) {
                // Start continuous task for ongoing operations
                boolean started = startContinuousTask();
                if (started) {
                    MsgUtility.log("Event " + eventName +
                            " started continuous inventory operations with interval of " +
                            operationInterval + " seconds");
                } else {
                    MsgUtility.log("Event " + eventName +
                            " tried to start continuous operations but it was already running");
                }
            } else {
                // Do a one-time operation for all players
                int affected = applyToAllPlayers();
                this.lastOperationCount = affected;

                MsgUtility.log("Event " + eventName +
                        " applied inventory operations to " + affected +
                        " players out of " + plugin.getServer().getOnlinePlayers().size());
            }
        } catch (Exception e) {
            MsgUtility.warning("Error applying inventory operations in " + eventName + ": " + e.getMessage());
        }
    }

    // Terminates the event
    @Override
    public void terminate() {
        boolean stopped = stopContinuousTask();

        if (stopped) {
            MsgUtility.log("Event " + eventName + " stopped continuous inventory operations");
        } else {
            MsgUtility.warning("Event " + eventName + " tried to stop continuous operations but it was already stopped");
        }
    }

    // Starts continuous task for ongoing operations
    public boolean startContinuousTask() {
        // Check if task is already running
        if (continuousTask != null && !continuousTask.isCancelled()) {
            return false;
        }

        // Use BukkitRunnable for continuous task that operates on all players
        continuousTask = new BukkitRunnable() {
            @Override
            public void run() {
                applyToAllPlayers();
            }
        }.runTaskTimer(plugin, 20L, operationInterval * 20L);

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

    // Called when an operation is performed (optional override for child classes)
    protected void onOperationPerformed(Player player) {
    }

    // Applies operations to all online players
    public int applyToAllPlayers() {
        int affectedPlayers = 0;
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        for (Player player : players) {
            boolean success = applyToPlayer(player);
            if (success) {
                affectedPlayers++;
                MsgUtility.log("Applied inventory operation for player " + player.getName());

                // Optional hook for child classes to implement additional logic
                onOperationPerformed(player);
            }
        }
        return affectedPlayers;
    }

    // Apply operations to a specific player (to be implemented by child classes)
    protected abstract boolean applyToPlayer(Player player);

    // Method to remove equipped item
    protected ItemStack unequipPlayerItem(Player player, EquipmentSlot slot) {
        PlayerInventory inventory = player.getInventory();

        ItemStack equippedItem = switch (slot) {
            case HEAD -> inventory.getHelmet();
            case CHEST -> inventory.getChestplate();
            case LEGS -> inventory.getLeggings();
            case FEET -> inventory.getBoots();
            case HAND -> inventory.getItemInMainHand();
            case OFF_HAND -> inventory.getItemInOffHand();
            default -> null;
        };

        if (equippedItem == null || equippedItem.getType() == Material.AIR) {
            return null;
        }

        // Store the equipped item
        ItemStack removedItem = equippedItem.clone();

        // Clear the specific equipment slot
        switch (slot) {
            case HEAD -> inventory.setHelmet(null);
            case CHEST -> inventory.setChestplate(null);
            case LEGS -> inventory.setLeggings(null);
            case FEET -> inventory.setBoots(null);
            case HAND -> inventory.setItemInMainHand(null);
            case OFF_HAND -> inventory.setItemInOffHand(null);
        }

        // Try to add it to player's inventory
        HashMap<Integer, ItemStack> leftover = inventory.addItem(removedItem);


        // If inventory is full, drop the item
        if (!leftover.isEmpty()) {
            for (ItemStack item : leftover.values()) {
                dropItem(item, player);
            }
        }

        return removedItem;
    }

    // Method to equip an item
    protected boolean equipPlayerItem(Player player, EquipmentSlot slot, ItemStack itemToEquip) {
        if (itemToEquip == null || itemToEquip.getType() == Material.AIR) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();

        // Remove any existing equipment
        unequipPlayerItem(player, slot);

        // Mark the item as equipped
        markItemStack(itemToEquip);

        return switch (slot) {
            case HEAD -> {
                inventory.setHelmet(itemToEquip);
                yield true;
            }
            case CHEST -> {
                inventory.setChestplate(itemToEquip);
                yield true;
            }
            case LEGS -> {
                inventory.setLeggings(itemToEquip);
                yield true;
            }
            case FEET -> {
                inventory.setBoots(itemToEquip);
                yield true;
            }
            case HAND -> {
                inventory.setItemInMainHand(itemToEquip);
                yield true;
            }
            case OFF_HAND -> {
                inventory.setItemInOffHand(itemToEquip);
                yield true;
            }
            default -> false;
        };
    }

    // Method to add items to a player's inventory
    protected List<Item> addPlayerInvItems(Player player, List<ItemStack> items) {
        List<Item> droppedItems = new ArrayList<>();
        PlayerInventory inventory = player.getInventory();

        for (ItemStack itemToAdd : items) {
            // Mark the item with PDC
            ItemStack markedItem = itemToAdd.clone();
            markItemStack(markedItem);

            // Try to add to inventory
            HashMap<Integer, ItemStack> leftover = inventory.addItem(markedItem);

            // If inventory is full, drop the leftover items
            if (!leftover.isEmpty()) {
                for (ItemStack item : leftover.values()) {
                    Item droppedItem = dropItem(item, player);
                    droppedItems.add(droppedItem);
                }
            }
        }
        return droppedItems;
    }

    // Method to add random items to a player's inventory
    protected List<Item> addRandomPlayerInvItems(Player player, int count) {
        List<ItemStack> itemsToAdd = new ArrayList<>();

        // Generate the specified number of random items
        for (int i = 0; i < count; i++) {
            ItemStack randomItem = getRandomWeightedItem();
            if (randomItem != null) {
                // Clone and mark each item
                ItemStack itemToAdd = randomItem.clone();
                itemsToAdd.add(itemToAdd);
            }
        }

        // Add all items to the player's inventory
        return addPlayerInvItems(player, itemsToAdd);
    }

    // Method to replace an item in the player's inventory
    public boolean replacePlayerInvItems(Player player, ItemStack itemToReplace, ItemStack itemReplacement) {
        // Get player inventory
        PlayerInventory inventory = player.getInventory();

        // Store the required amounts
        int toReplaceAmount = itemToReplace.getAmount();
        int replacementAmount = itemReplacement.getAmount();

        // Count how many of the item the player has
        int totalAvailable = countItem(inventory, itemToReplace);

        if (totalAvailable < toReplaceAmount) {
            return false;
        }

        // Calculate how many replacements we can make
        int replacementSets = totalAvailable / toReplaceAmount;

        // Create a copy with the total amount to remove
        ItemStack itemsToRemove = itemToReplace.clone();
        itemsToRemove.setAmount(replacementSets * toReplaceAmount);

        // Remove the items
        deletePlayerInvItems(inventory, itemsToRemove);

        // Add the replacement items
        ItemStack replacementCopy = itemReplacement.clone();
        replacementCopy.setAmount(replacementAmount * replacementSets);

        // Add items to inventory or drop them if inventory is full
        HashMap<Integer, ItemStack> leftover = inventory.addItem(replacementCopy);
        if (!leftover.isEmpty()) {
            for (ItemStack item : leftover.values()) {
                dropItem(item, player);
            }
        }

        return true;
    }

    // Method to delete items from player's inventory
    private void deletePlayerInvItems(PlayerInventory inventory, ItemStack itemToRemove) {
        int remaining = itemToRemove.getAmount();
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length && remaining > 0; i++) {
            ItemStack item = contents[i];
            if (item != null && item.isSimilar(itemToRemove)) {
                if (item.getAmount() <= remaining) {
                    remaining -= item.getAmount();
                    inventory.setItem(i, null);
                } else {
                    item.setAmount(item.getAmount() - remaining);
                    remaining = 0;
                }
            }
        }
    }

    // Method to drop a specified item from the player's inventory
    public int dropPlayerInvItems(Player player, ItemStack itemToDrop) {
        PlayerInventory inventory = player.getInventory();

        // Store the amount to drop
        int amountToDrop = itemToDrop.getAmount();

        // Count how many of the item the player has
        int totalAvailable = countItem(inventory, itemToDrop);

        if (totalAvailable <= 0) {
            return 0;
        }

        // Determine how many we can actually drop
        int actualDropAmount = Math.min(totalAvailable, amountToDrop);

        // Create a copy with the actual amount to delete
        ItemStack itemsToDelete = itemToDrop.clone();
        itemsToDelete.setAmount(actualDropAmount);

        // Delete the items
        deletePlayerInvItems(inventory, itemsToDelete);

        // Create the item to drop
        ItemStack itemToDropCopy = itemToDrop.clone();
        itemToDropCopy.setAmount(actualDropAmount);

        // Drop the items
        dropItem(itemToDropCopy, player);

        return actualDropAmount;
    }

    // Method to drop an items at the player's location
    public Item dropItem(ItemStack itemToDrop, Player player) {
        if (player == null || itemToDrop == null || itemToDrop.getType() == Material.AIR) {
            return null;
        }

        return player.getWorld().dropItemNaturally(player.getLocation(), itemToDrop);
    }

    // Method to count an item in the player's inventory
    private int countItem(PlayerInventory inventory, ItemStack itemToCount) {
        int count = 0;
        ItemStack[] contents = inventory.getContents();

        for (ItemStack item : contents) {
            if (item != null && item.isSimilar(itemToCount)) {
                count += item.getAmount();
            }
        }

        return count;
    }

    // Method to get a random weighted item
    protected ItemStack getRandomWeightedItem() {
        if (weightedItems.isEmpty()) {
            return null;
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

    // Method to add multiple weighted items at once
    public BaseInventoryAdjustment addWeightedItems(List<Pair<ItemStack, Double>> items) {
        if (items != null) {
            weightedItems.addAll(items);
        }
        return this;
    }

    // Removes a weighted item
    public BaseInventoryAdjustment removeWeightedItem(ItemStack itemToRemove) {
        if (itemToRemove != null) {
            weightedItems.removeIf(pair -> pair.getLeft().isSimilar(itemToRemove));
        }
        return this;
    }

    // Method to set the entire list of weighted items
    public BaseInventoryAdjustment setWeightedItems(List<Pair<ItemStack, Double>> items) {
        // Clear existing items
        weightedItems.clear();

        // Add new ones if not null
        if (items != null) {
            weightedItems.addAll(items);
        }

        return this;
    }

    // Method to mark a dropped item entity
    public void markItem(Item item) {
        item.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    // Method to mark an ItemStack
    public void markItemStack(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
    }

    // Method to check if an item is marked
    protected boolean isItemMarked(Item item) {
        return item.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    // Method to check if an ItemStack is marked
    protected boolean isItemStackMarked(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    // Method to delete all marked dropped items
    protected void deleteMarkedItems() {
        EventHorizon.entityKeysToDelete.add(key);
        Bukkit.getWorlds().forEach(world -> {
            world.getEntitiesByClass(Item.class).forEach(item -> {
                if (isItemMarked(item)) {
                    item.remove();
                }
            });
        });
    }

    // Method to delete all marked items inside the player's inventory
    protected void deleteMarkedItemStacks() {
        EventHorizon.entityKeysToDelete.add(key);
        plugin.getServer().getOnlinePlayers().forEach(PlayerInventoryListener::removeMarkedItems);
    }

    // Getters
    public int getLastOperationCount() {
        return lastOperationCount;
    }

    // Setters
    public BaseInventoryAdjustment setItemType(ItemStack itemType) {
        this.itemType = itemType;
        return this;
    }

    public BaseInventoryAdjustment setUseContinuousOperation(boolean continuousOperation) {
        this.useContinuousOperation = continuousOperation;
        return this;
    }

    public BaseInventoryAdjustment setOperationInterval(int seconds) {
        this.operationInterval = seconds;
        return this;
    }
}
