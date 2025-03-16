package capstone.team1.eventHorizon.events.utility;

import capstone.team1.eventHorizon.utility.MsgUtil;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.sk89q.worldedit.regions.Region;

import java.util.ArrayList;
import java.util.List;

public class FAWEUtil {

    private static List<EditSession> activeEditSessions = new ArrayList<>();

    public static CylinderRegion selectCylindricalRegionAroundPlayer(Player player, int radius, int height) {
        // Convert Bukkit Location to WorldEdit BlockVector3
        Location playerLoc = player.getLocation();
        BlockVector3 center = BlockVector3.at(
                playerLoc.getBlockX(),
                playerLoc.getBlockY(),
                playerLoc.getBlockZ()
        );

        // Create a cylindrical region around the player
        CylinderRegion region = new CylinderRegion(
                center, // Center point
                new Vector2(radius, radius), // Radius of cylinder
                center.y() - height / 2, // Minimum Y value
                center.y() + height / 2  // Maximum Y value
        );

        return region;
    }

    public static CuboidRegion selectCuboidRegionAroundPlayer(Player player, int radius, int height) {
        // Convert Bukkit Location to WorldEdit BlockVector3
        Location playerLoc = player.getLocation();
        BlockVector3 center = BlockVector3.at(
                playerLoc.getBlockX(),
                playerLoc.getBlockY(),
                playerLoc.getBlockZ()
        );

        // Create a cuboid region around the player
        CuboidRegion region = new CuboidRegion(
                center.subtract(radius, height / 2, radius), // Minimum point
                center.add(radius, height / 2, radius) // Maximum point
        );

        return region;
    }
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
            editSession.setBlocks(region, pattern);
            Operations.complete(editSession.commit());

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