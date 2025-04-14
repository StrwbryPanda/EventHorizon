package capstone.team1.eventHorizon.events.dropModification;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
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
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            isActive = true;
            MsgUtility.log("Event " + eventName + " started drop modifications");
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
            MsgUtility.log("Event " + eventName + " terminated");
        }
    }

    protected abstract void setupDropModifications();

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

    protected void dropItem(ItemStack item, Location location) {
        if (item == null || location == null || location.getWorld() == null) return;
        ItemStack toDrop = item.clone();
        location.getWorld().dropItemNaturally(location, toDrop);
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
        return blockDrops.get(blockType);
    }

    public List<ItemStack> getMobDrops(EntityType mobType) {
        return mobDrops.get(mobType);
    }

    public boolean isActive() {
        return isActive;
    }
}