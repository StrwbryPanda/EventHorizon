package capstone.team1.eventHorizon.events.dropModification;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Event that shuffles block drops to random fixed items
 */
public class BlockDropShuffle extends BaseDropModification {
    public BlockDropShuffle() {
        super(EventClassification.NEUTRAL, "blockDropShuffle");
    }

    @Override
    protected void setupDropModifications() {
        List<ItemStack> survivalDropPool = generateSurvivalDropsList();

        for (Material material : Material.values()) {
            if (material.isBlock() && material != Material.AIR) {
                setFixedBlockDrop(material, survivalDropPool);
            }
        }
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
