package capstone.team1.eventHorizon.events.itemSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * A positive event that spawns random weighted ore items near players.
 * Ore items have different spawn weights, with rarer ores having lower weights.
 * The items are spawned within configured radius limits and following specific spawn rules.
 */
public class OreDropParty extends BaseItemSpawn {
    /**
     * Constructs a new OreDropParty event with predefined spawn configurations and weighted ore items.
     * The event is classified as POSITIVE and named "oreDropParty".
     * Initializes spawn parameters including:
     * - 32 items per spawn
     * - Spawn radius between 1-20 blocks
     * - Vertical spawn range between 1-10 blocks
     * - Various ore items with weights from 0.5 (rarest) to 15.0 (most common)
     */
    public OreDropParty() {
        super(EventClassification.POSITIVE, "oreDropParty");
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
                        Pair.of(new ItemStack(Material.ANCIENT_DEBRIS), 0.5),
                        Pair.of(new ItemStack(Material.EMERALD), 1.0),
                        Pair.of(new ItemStack(Material.DIAMOND), 1.0),
                        Pair.of(new ItemStack(Material.AMETHYST_SHARD), 2.0),
                        Pair.of(new ItemStack(Material.QUARTZ), 2.0),
                        Pair.of(new ItemStack(Material.RAW_GOLD), 5.0),
                        Pair.of(new ItemStack(Material.LAPIS_LAZULI), 5.0),
                        Pair.of(new ItemStack(Material.REDSTONE), 5.0),
                        Pair.of(new ItemStack(Material.RAW_IRON), 10.0),
                        Pair.of(new ItemStack(Material.RAW_COPPER), 15.0),
                        Pair.of(new ItemStack(Material.COAL), 15.0)
                ));
    }

    /**
     * Executes the OreDropParty event, spawning configured ore items near players.
     * Calls the parent class's execute method to handle the actual spawning logic.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the OreDropParty event, cleaning up any ongoing processes.
     * Calls the parent class's terminate method to handle the cleanup.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}