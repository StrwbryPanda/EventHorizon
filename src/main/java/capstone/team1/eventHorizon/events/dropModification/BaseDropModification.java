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
import org.bukkit.plugin.Plugin;

import java.util.*;

public abstract class BaseDropModification extends BaseEvent implements Listener {
    protected final Plugin plugin;
    protected final Random random = new Random();
    protected final NamespacedKey key;

    // Drop modification maps
    protected final Map<Material, List<ItemStack>> blockDrops = new HashMap<>();
    protected final Map<EntityType, List<ItemStack>> mobDrops = new HashMap<>();

    // Flag to check if the event is active
    protected boolean isActive = false;

    public BaseDropModification(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);
    }

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

    @Override
    public void terminate() {
        if (isActive) {
            HandlerList.unregisterAll(this);
            disableDropModifications();
            isActive = false;
            MsgUtility.log("<red>Terminating drop modification event: " + this.eventName);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isActive || event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        // Make sure drops are enabled to trigger BlockDropItemEvent
        event.setDropItems(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockDropItem(BlockDropItemEvent event) {
        handleBlockDrops(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event) {
        handleEntityDrops(event);
    }

    protected abstract void setupDropModifications();

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

        // Create a new list to store the modified drops
        List<ItemStack> modifiedDrops = new ArrayList<>();

        // Process each original drop individually
        for (ItemStack originalItem : originalDrops) {
            // Skip empty items
            if (originalItem == null || originalItem.getType() == Material.AIR) {
                continue;
            }

            // Get quantity of the original item (respects Looting)
            int quantity = originalItem.getAmount();

            // Select a random custom drop for this item
            ItemStack customDrop = selectRandomDrop(customDrops);
            if (customDrop != null) {
                // Apply the original quantity to preserve Looting effects
                customDrop = customDrop.clone();
                customDrop.setAmount(quantity);
                modifiedDrops.add(customDrop);

                MsgUtility.log("<green>Entity " + entityType + ": " +
                        originalItem.getType() + " x" + quantity + " → " +
                        customDrop.getType() + " x" + customDrop.getAmount());
            }
        }

        // Clear original drops and add our modified ones
        originalDrops.clear();
        originalDrops.addAll(modifiedDrops);

        return true;
    }

    public void setFixedBlockDrop(Material blockType, List<ItemStack> possibleDrops) {
        if (possibleDrops == null || possibleDrops.isEmpty()) {
            MsgUtility.warning("No possible drops provided for block type: " + blockType);
            return;
        }
        blockDrops.put(blockType, Collections.singletonList(
                possibleDrops.get(random.nextInt(possibleDrops.size())).clone()
        ));
    }

    public void setFixedMobDrop(EntityType mobType, List<ItemStack> possibleDrops) {
        if (possibleDrops == null || possibleDrops.isEmpty()) {
            MsgUtility.warning("No possible drops provided for mob type: " + mobType);
            return;
        }
        mobDrops.put(mobType, Collections.singletonList(
                possibleDrops.get(random.nextInt(possibleDrops.size())).clone()
        ));
    }

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

    protected ItemStack selectRandomDrop(List<ItemStack> drops) {
        if (drops == null || drops.isEmpty()) {
            return null;
        }
        return drops.get(random.nextInt(drops.size())).clone();
    }

    protected void dropItem(ItemStack item, Location location) {
        if (item == null || location == null || location.getWorld() == null) return;

        // Center the location
        Location centerLoc = location.clone().add(0.5, 0.5, 0.5);

        ItemStack toDrop = item.clone();
        location.getWorld().dropItemNaturally(centerLoc, toDrop);
    }

    protected void dropItems(List<ItemStack> items, Location location) {
        if (items == null || items.isEmpty() || location == null || location.getWorld() == null) return;
        for (ItemStack item : items) {
            dropItem(item, location);
        }
    }

    public void addBlockDrop(Material blockType, ItemStack drop) {
        if (drop == null || blockType == null) return;

        blockDrops.computeIfAbsent(blockType, k -> new ArrayList<>(1)).add(drop.clone());
    }

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

    public void addMobDrop(EntityType mobType, ItemStack drop) {
        if (drop == null || mobType == null) return;

        mobDrops.computeIfAbsent(mobType, k -> new ArrayList<>(1)).add(drop.clone());
    }

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

    public void disableDropModifications() {
        blockDrops.clear();
        mobDrops.clear();
        MsgUtility.log("Disabled all custom drop modifications for event: " + this.eventName);
    }

    public List<ItemStack> getBlockDrops(Material blockType) {
        List<ItemStack> drops = blockDrops.get(blockType);
        return drops != null ? drops : Collections.emptyList();
    }

    public List<ItemStack> getMobDrops(EntityType mobType) {
        List<ItemStack> drops = mobDrops.get(mobType);
        return drops != null ? drops : Collections.emptyList();
    }

    public boolean isActive() {
        return isActive;
    }
}