package capstone.team1.eventHorizon.events.utility.fawe;

import capstone.team1.eventHorizon.utility.MsgUtility;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlockEditor
{
    private static List<EditSession> activeEditSessions = new ArrayList<>();

    public static void replaceBlocksInRegion(Region region, Material blockId, Collection<BlockType> blockTypesToReplace, boolean isMaskInverted) {
        try {
            com.sk89q.worldedit.world.World world = region.getWorld();
            EditSession editSession = WorldEdit.getInstance()
                    .newEditSessionBuilder()
                    .world(world)
                    .maxBlocks(-1)
                    .build();

            BlockType blockType = BukkitAdapter.asBlockType(blockId);
            if (blockType == null) {
                throw new IllegalArgumentException("Invalid block ID: " + blockId);
            }

            Pattern pattern = blockType.getDefaultState();

            // Create a mask that excludes air blocks and GUI blocks
            BlockTypeMask mask = new BlockTypeMask(editSession, blockTypesToReplace);

            editSession.replaceBlocks(region, isMaskInverted ? mask.inverse() : mask, pattern);
//            editSession.setBlocks(region, pattern);
            Operations.complete(editSession.commit());
            editSession.flushQueue();
            activeEditSessions.add(editSession);
        } catch (Exception e) {
            MsgUtility.warning("Failed to replace blocks: " + e.getMessage());
        }
    }

    public static void undoAllBlockModifications() {
        try {
            for (EditSession session : activeEditSessions) {
                session.undo(session);
                Operations.complete(session.commit());
                session.close();
            }
            activeEditSessions.clear();
        } catch (Exception e) {
            MsgUtility.warning("Failed to undo block modifications: " + e.getMessage());
        }
    }
}
