package capstone.team1.eventHorizon.events.dropModification;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Base class for handling custom drop modifications in the game.
 * This class provides functionality to modify drops from blocks and entities,
 * handling events such as block breaking and entity death.
 * Extends BaseEvent and implements Listener for event handling.
 */
public abstract class BaseDropModification extends BaseEvent implements Listener {
    /** Reference to the main plugin instance */
    EventHorizon plugin = EventHorizon.getPlugin();
    /** Random number generator for drop selection */

    protected final Random random = new Random();
    /** Unique identifier key for this modification */

    protected final NamespacedKey key;

    // Drop modification maps
    /** Maps block materials to their possible custom drops */

    protected final Map<Material, List<ItemStack>> blockDrops = new HashMap<>();
    /** Maps entity types to their possible custom drops */

    protected final Map<EntityType, List<ItemStack>> mobDrops = new HashMap<>();

    // Flag to check if the event is active
    /** Flag indicating whether this drop modification is currently active */

    protected boolean isActive = false;

    /**
     * Constructs a BaseDropModification with the specified classification and event name.
     * Initializes the namespaced key for this modification.
     *
     * @param classification the event classification (POSITIVE/NEGATIVE)
     * @param eventName the unique name for this event
     */
    public BaseDropModification(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    /**
     * Executes the drop modification event by setting up modifications and registering event listeners.
     * Logs success or failure of the execution.
     */
    @Override
    public void execute() {
        try {
            setupDropModifications();
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            isActive = true;
            MsgUtility.log("<green>Executing drop modification event: " + this.eventName);
        } catch (Exception e) {
            MsgUtility.warning("Error starting drop modifications in " + eventName + ": " + e.getMessage());
        }
    }

    /**
     * Terminates the drop modification event by unregistering listeners and clearing modifications.
     * Only executes if the event is currently active.
     */
    @Override
    public void terminate() {
        if (isActive) {
            HandlerList.unregisterAll(this);
            disableDropModifications();
            isActive = false;
            MsgUtility.log("<red>Terminating drop modification event: " + this.eventName);
        }
    }

    /**
     * Handles block breaking events by ensuring drops are enabled for non-creative players.
     *
     * @param event the block break event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isActive || event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        // Make sure drops are enabled to trigger BlockDropItemEvent
        event.setDropItems(true);
    }

    /**
     * Processes item drops from broken blocks by applying custom drop modifications.
     *
     * @param event the block drop item event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockDropItem(BlockDropItemEvent event) {
        handleBlockDrops(event);
    }

    /**
     * Handles entity death events by modifying their drops according to custom rules.
     *
     * @param event the entity death event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event) {
        handleEntityDrops(event);
    }

    /**
     * Abstract method to be implemented by subclasses to set up their specific drop modifications.
     */
    protected abstract void setupDropModifications();

    /**
     * Processes and applies custom drops for broken blocks.
     *
     * @param event the block drop item event
     * @return true if custom drops were applied, false otherwise
     */
    protected boolean handleBlockDrops(BlockDropItemEvent event) {
        if (!isActive || event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return false;
        }

        if (event.getItems().isEmpty()) {
            return false;
        }

        Material blockType = event.getBlockState().getType();
        List<ItemStack> customDrops = getBlockDrops(blockType);

        if (customDrops != null && !customDrops.isEmpty()) {
            // Calculate total quantity - respects Fortune
            int totalQuantity = 0;
            for (Item item : event.getItems()) {
                totalQuantity += item.getItemStack().getAmount();
            }

            if (totalQuantity > 0) {
                // Cancel original drops
                event.setCancelled(true);

                // Select a random drop and apply quantity
                ItemStack customDrop = selectRandomDrop(customDrops);
                customDrop.setAmount(totalQuantity);

                // Drop the item
                dropItem(customDrop, event.getBlock().getLocation());

                return true;
            }
        }
        return false;
    }

    /**
     * Processes and applies custom drops for killed entities.
     *
     * @param event the entity death event
     * @return true if custom drops were applied, false otherwise
     */
    protected boolean handleEntityDrops(EntityDeathEvent event) {
        if (!isActive) {
            return false;
        }

        EntityType entityType = event.getEntityType();
        List<ItemStack> customDrops = getMobDrops(entityType);

        if (customDrops == null || customDrops.isEmpty()) {
            return false;
        }

        // Get the original drops
        List<ItemStack> originalDrops = event.getDrops();

        // If there are no original drops, nothing to modify
        if (originalDrops.isEmpty()) {
            return false;
        }

        // Create a copy of the original drops to iterate through
        List<ItemStack> originalDropsCopy = new ArrayList<>(originalDrops);

        // Clear original drops list so we can add modified ones
        originalDrops.clear();

        // Process each original drop individually
        for (ItemStack originalItem : originalDropsCopy) {
            // Skip empty items
            if (originalItem == null || originalItem.getType() == Material.AIR) {
                continue;
            }

            // Get quantity of the original item (respects Looting)
            int quantity = originalItem.getAmount();

            // Generate a deterministic index based on the material type
            // This ensures the same original material always maps to the same replacement
            int deterministicIndex = Math.abs(originalItem.getType().name().hashCode()) % customDrops.size();

            // Use the deterministic index to select the replacement
            ItemStack customDrop = customDrops.get(deterministicIndex).clone();
            customDrop.setAmount(quantity);
            originalDrops.add(customDrop);
        }
        return true;
    }

    /**
     * Sets a fixed drop for a specific block type from a list of possible drops.
     *
     * @param blockType the material type of the block
     * @param possibleDrops list of possible items to drop
     */
    public void setFixedBlockDrop(Material blockType, List<ItemStack> possibleDrops) {
        if (possibleDrops == null || possibleDrops.isEmpty()) {
            MsgUtility.warning("No possible drops provided for block type: " + blockType);
            return;
        }
        blockDrops.put(blockType, Collections.singletonList(
                possibleDrops.get(random.nextInt(possibleDrops.size())).clone()
        ));
    }

    /**
     * Sets fixed drops for a specific mob type from a list of possible drops.
     *
     * @param mobType the type of entity
     * @param possibleDrops list of possible items to drop
     */
    public void setFixedMobDrop(EntityType mobType, List<ItemStack> possibleDrops) {
        if (possibleDrops == null || possibleDrops.isEmpty()) {
            MsgUtility.warning("No possible drops provided for mob type: " + mobType);
            return;
        }

        List<ItemStack> allPossibleDrops = new ArrayList<>(possibleDrops);
        mobDrops.put(mobType, allPossibleDrops);
    }

    /**
     * Generates a list of all possible item drops in survival mode.
     *
     * @return list of all valid item stacks
     */
    public List<ItemStack> generateSurvivalDropsList() {
        List<ItemStack> drops = new ArrayList<>();
        for (Material material : Material.values()) {
            // Check if the material can be represented as an item and is not AIR.
            if (material.isItem() && material != Material.AIR) {
                drops.add(new ItemStack(material));
            }
        }
        return drops;
    }

    /**
     * Selects a random item stack from a list of possible drops.
     *
     * @param drops list of possible drops
     * @return randomly selected ItemStack or null if list is empty
     */
    protected ItemStack selectRandomDrop(List<ItemStack> drops) {
        if (drops == null || drops.isEmpty()) {
            return null;
        }
        return drops.get(random.nextInt(drops.size())).clone();
    }

    /**
     * Drops a single item at a specified location.
     *
     * @param item the item to drop
     * @param location the location to drop the item
     */
    protected void dropItem(ItemStack item, Location location) {
        if (item == null || location == null || location.getWorld() == null) return;

        // Center the location
        Location centerLoc = location.clone().add(0.5, 0.5, 0.5);

        ItemStack toDrop = item.clone();
        location.getWorld().dropItemNaturally(centerLoc, toDrop);
    }

    /**
     * Drops multiple items at a specified location.
     *
     * @param items list of items to drop
     * @param location the location to drop the items
     */
    protected void dropItems(List<ItemStack> items, Location location) {
        if (items == null || items.isEmpty() || location == null || location.getWorld() == null) return;
        for (ItemStack item : items) {
            dropItem(item, location);
        }
    }

    /**
     * Adds a single drop to the possible drops for a block type.
     *
     * @param blockType the material type of the block
     * @param drop the item to add as a possible drop
     */
    public void addBlockDrop(Material blockType, ItemStack drop) {
        if (drop == null || blockType == null) return;

        blockDrops.computeIfAbsent(blockType, k -> new ArrayList<>(1)).add(drop.clone());
    }

    /**
     * Sets all possible drops for a specific block type.
     *
     * @param blockType the material type of the block
     * @param drops list of possible drops
     */
    public void setBlockDrops(Material blockType, List<ItemStack> drops) {
        if (blockType == null) return;
        if (drops == null || drops.isEmpty()) {
            blockDrops.remove(blockType);
        } else {
            ArrayList<ItemStack> newDrops = new ArrayList<>(drops.size());
            for (ItemStack item : drops) {
                if (item != null) {
                    newDrops.add(item.clone());
                }
            }
            blockDrops.put(blockType, newDrops);
        }
    }

    /**
     * Adds a single drop to the possible drops for a mob type.
     *
     * @param mobType the type of entity
     * @param drop the item to add as a possible drop
     */
    public void addMobDrop(EntityType mobType, ItemStack drop) {
        if (drop == null || mobType == null) return;

        mobDrops.computeIfAbsent(mobType, k -> new ArrayList<>(1)).add(drop.clone());
    }

    /**
     * Sets all possible drops for a specific mob type.
     *
     * @param mobType the type of entity
     * @param drops list of possible drops
     */
    public void setMobDrops(EntityType mobType, List<ItemStack> drops) {
        if (mobType == null) return;
        if (drops == null || drops.isEmpty()) {
            mobDrops.remove(mobType);
        } else {
            ArrayList<ItemStack> newDrops = new ArrayList<>(drops.size());
            for (ItemStack item : drops) {
                if (item != null) {
                    newDrops.add(item.clone());
                }
            }
            mobDrops.put(mobType, newDrops);
        }
    }

    /**
     * Disables all custom drop modifications and clears drop maps.
     */
    public void disableDropModifications() {
        blockDrops.clear();
        mobDrops.clear();
        MsgUtility.log("Disabled all custom drop modifications for event: " + this.eventName);
    }

    /**
     * Retrieves the list of possible drops for a specific block type.
     *
     * @param blockType the material type of the block
     * @return list of possible drops or empty list if none exist
     */
    public List<ItemStack> getBlockDrops(Material blockType) {
        List<ItemStack> drops = blockDrops.get(blockType);
        return drops != null ? drops : Collections.emptyList();
    }

    /**
     * Retrieves the list of possible drops for a specific mob type.
     *
     * @param mobType the type of entity
     * @return list of possible drops or empty list if none exist
     */
    public List<ItemStack> getMobDrops(EntityType mobType) {
        List<ItemStack> drops = mobDrops.get(mobType);
        return drops != null ? drops : Collections.emptyList();
    }

    /**
     * Checks if this drop modification is currently active.
     *
     * @return true if the modification is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
}