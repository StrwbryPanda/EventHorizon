package capstone.team1.eventHorizon.events.dropModification;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Event that shuffles block drops to random fixed items
 */
public class BlockDropShuffle extends BaseDropModification {
    public BlockDropShuffle() {
        super(EventClassification.NEUTRAL, "blockDropShuffle");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        // Skip if the event is not active or player is in creative mode
        if (!isActive || event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        Block block = event.getBlock();
        Material blockType = block.getType();
        List<ItemStack> customDrops = getBlockDrops(blockType);

        if (customDrops != null && !customDrops.isEmpty()) {
            event.setDropItems(false);

            dropItems(customDrops, block.getLocation());
        }
    }

    @Override
    protected void setupDropModifications() {
        List<ItemStack> survivalDropPool = generateSurvivalDropsList();

        int count = 0;
        for (Material material : Material.values()) {
            if (material.isBlock() && material != Material.AIR && material.isOccluding()) {
                setFixedBlockDrop(material, survivalDropPool);
                count++;
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
