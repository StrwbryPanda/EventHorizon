package capstone.team1.eventHorizon.events.itemSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Event that spawns random weighted ore drops near players
 */
public class OreDropParty extends BaseItemSpawn {
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

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
    }
}
