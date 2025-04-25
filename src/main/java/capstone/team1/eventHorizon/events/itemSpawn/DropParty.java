package capstone.team1.eventHorizon.events.itemSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DropParty extends BaseItemSpawn {
    private List<ItemStack> dropItems = new ArrayList<>();

    public DropParty() {
        super(EventClassification.POSITIVE, "dropParty");
        setItemCount(32)
                .setMaxSpawnRadius(20)
                .setMinSpawnRadius(1)
                .setMaxYRadius(10)
                .setMinYRadius(1)
                .setMaxSpawnAttempts(20)
                .setHeightClearance(1)
                .setWidthClearance(1)
                .setSurfaceOnlySpawning(false)
                .setAllowWaterSpawns(false)
                .setAllowLavaSpawns(false)
                .setRandomItemTypes(true);
        dropItems.addAll(generateDropsList());
    }

    public List<ItemStack> generateDropsList() {
        List<ItemStack> drops = new ArrayList<>();
        for (Material material : Material.values()) {
            // Check if the material can be represented as an item and is not AIR.
            if (material.isItem() && material != Material.AIR) {
                drops.add(new ItemStack(material));
            }
        }
        return drops;
    }

    @Override
    protected ItemStack getRandomWeightedItem() {
        // Return a random item from our list
        if (dropItems.isEmpty()) {
            // Fallback to a default item if the list is somehow empty
            return new ItemStack(Material.STONE);
        }
        return dropItems.get(random.nextInt(dropItems.size()));
    }
}