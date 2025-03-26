package capstone.team1.eventHorizon.events.itemSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Feast extends BaseItemSpawn {
    public Feast() {
        super(EventClassification.POSITIVE, "feast");
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
                .setUseGroupSpawning(false)
                .setGroupSpacing(2)
                .setUseContinuousSpawning(false)
                .setSpawnInterval(60)
                .setRandomItemTypes(true)
                .setWeightedItems(Arrays.asList(
                        Pair.of(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), 0.5),
                        Pair.of(new ItemStack(Material.GOLDEN_APPLE), 1.0),
                        Pair.of(new ItemStack(Material.GOLDEN_CARROT), 1.0),
                        Pair.of(new ItemStack(Material.CAKE), 1.0),

                        Pair.of(new ItemStack(Material.COOKIE), 2.0),
                        Pair.of(new ItemStack(Material.MELON), 2.0),
                        Pair.of(new ItemStack(Material.SWEET_BERRIES), 2.0),
                        Pair.of(new ItemStack(Material.ROTTEN_FLESH), 2.0),

                        Pair.of(new ItemStack(Material.COOKED_MUTTON), 3.0),
                        Pair.of(new ItemStack(Material.COOKED_PORKCHOP), 3.0),
                        Pair.of(new ItemStack(Material.COOKED_SALMON), 3.0),
                        Pair.of(new ItemStack(Material.COOKED_BEEF), 3.0),

                        Pair.of(new ItemStack(Material.BAKED_POTATO), 5.0),
                        Pair.of(new ItemStack(Material.BREAD), 5.0),
                        Pair.of(new ItemStack(Material.COOKED_CHICKEN), 5.0),
                        Pair.of(new ItemStack(Material.COOKED_COD), 5.0),
                        Pair.of(new ItemStack(Material.COOKED_RABBIT), 5.0),

                        Pair.of(new ItemStack(Material.APPLE), 5.0),
                        Pair.of(new ItemStack(Material.CARROT), 5.0)
                ));
    }

    @Override
    public void onItemSpawned(Item item, Player player) {
        item.setGlowing(true);
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
        deleteSpawnedItems();
    }
}
