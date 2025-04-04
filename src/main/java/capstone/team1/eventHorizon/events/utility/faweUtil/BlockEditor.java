package capstone.team1.eventHorizon.events.utility.faweUtil;

import capstone.team1.eventHorizon.utility.MsgUtil;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;

import java.util.ArrayList;
import java.util.List;

public class BlockEditor
{
    private static List<EditSession> activeEditSessions = new ArrayList<>();

    public static void replaceBlocksInRegion(Region region, String blockId) {
        try {
            com.sk89q.worldedit.world.World world = region.getWorld();
            EditSession editSession = WorldEdit.getInstance()
                    .newEditSessionBuilder()
                    .world(world)
                    .maxBlocks(-1)
                    .build();

            BlockType blockType = BlockTypes.get(blockId);
            if (blockType == null) {
                throw new IllegalArgumentException("Invalid block ID: " + blockId);
            }

            Pattern pattern = blockType.getDefaultState();

            // Create a mask that excludes air blocks and GUI blocks
            BlockTypeMask mask = new BlockTypeMask(editSession, BlockMasks.groundBlocks);


            editSession.replaceBlocks(region, mask, pattern);
            Operations.complete(editSession.commit());
            editSession.flushQueue();
            activeEditSessions.add(editSession);
        } catch (Exception e) {
            MsgUtil.warning("Failed to replace blocks: " + e.getMessage());
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
            MsgUtil.warning("Failed to undo block modifications: " + e.getMessage());
        }
    }
}
