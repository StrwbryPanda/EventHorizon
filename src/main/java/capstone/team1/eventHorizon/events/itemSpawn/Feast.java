package capstone.team1.eventHorizon.events.itemSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * A positive event that spawns random weighted food items near players.
 * Food items have different spawn weights, with rarer items having lower weights.
 * The items are spawned within configured radius limits and following specific spawn rules.
 */
public class Feast extends BaseItemSpawn {
    /**
     * Constructs a new Feast event with predefined spawn configurations and weighted food items.
     * The event is classified as POSITIVE and named "feast".
     * Initializes spawn parameters including:
     * - 32 items per spawn
     * - Spawn radius between 1-20 blocks
     * - Vertical spawn range between 1-10 blocks
     * - Various food items with weights from 0.5 (rarest) to 5.0 (most common)
     */
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

    /**
     * Executes the Feast event, spawning configured food items near players.
     * Calls the parent class's execute method to handle the actual spawning logic.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the Feast event, cleaning up any ongoing processes.
     * Calls the parent class's terminate method to handle the cleanup.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}