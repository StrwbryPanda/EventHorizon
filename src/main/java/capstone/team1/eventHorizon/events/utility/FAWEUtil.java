package capstone.team1.eventHorizon.events.utility;

import capstone.team1.eventHorizon.utility.MsgUtil;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.MaskUnion;
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
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.Masks;
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

        // Create a cylindrical region around the player with proper world adaptation
        CylinderRegion region = new CylinderRegion(
                BukkitAdapter.adapt(player.getWorld()),  // Convert Bukkit World to WorldEdit World
                center,
                new Vector2(radius, radius),
                center.y() - height / 2,
                center.y() + height / 2
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

        // Create a cuboid region around the player with proper world adaptation
        CuboidRegion region = new CuboidRegion(
                BukkitAdapter.adapt(player.getWorld()),  // Convert Bukkit World to WorldEdit World
                center.subtract(radius, height / 2, radius),
                center.add(radius, height / 2, radius)
        );

        return region;
    }
    public static CylinderRegion selectCylindricalRegionAtPlayersFeet(Player player, int radius, int height) {
        // Convert Bukkit Location to WorldEdit BlockVector3
        Location playerLoc = player.getLocation();
        BlockVector3 center = BlockVector3.at(
                playerLoc.getBlockX(),
                playerLoc.getBlockY(),
                playerLoc.getBlockZ()
        );

        // Create a cylindrical region around the player with proper world adaptation
        CylinderRegion region = new CylinderRegion(
                BukkitAdapter.adapt(player.getWorld()),
                center,
                new Vector2(radius, radius),
                center.y() - height,  // Start height blocks below player's feet
                center.y() - 1           // End at player's feet
        );

        return region;
    }
    public static CuboidRegion selectCuboidRegionAtPlayersFeet(Player player, int radius, int height) {
        // Convert Bukkit Location to WorldEdit BlockVector3
        Location playerLoc = player.getLocation();
        BlockVector3 center = BlockVector3.at(
                playerLoc.getBlockX(),
                playerLoc.getBlockY(),
                playerLoc.getBlockZ()
        );

        // Create a cuboid region below the player with proper world adaptation
        CuboidRegion region = new CuboidRegion(
                BukkitAdapter.adapt(player.getWorld()),
                center.subtract(radius, height, radius),  // Start height blocks below and expand outward
                center.add(radius, -1, radius)            // End at player's feet
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

            // Create a mask that excludes air blocks and GUI blocks

            editSession.replaceBlocks(region, insertmaskhere, pattern);
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