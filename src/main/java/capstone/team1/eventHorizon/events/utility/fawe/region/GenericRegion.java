package capstone.team1.eventHorizon.events.utility.fawe.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * An abstract base class for defining regions in WorldEdit operations.
 * This class provides common functionality for creating and managing different types of regions.
 */
public abstract class GenericRegion
{
    /** The radius of the region */
    protected int radius;
    /** The height of the region */
    protected int height;
    /** The vertical offset from the center point */
    protected int heightOffset;
    /** The block type ID to use for replacements */
    protected String replacementBlockId;
    /** Collection of block types that can be replaced in this region */
    protected Collection<BlockType> blockTypesToReplace;

    /**
     * Constructs a new region with specified dimensions.
     *
     * @param radius The radius of the region
     * @param height The height of the region
     * @param heightOffset The vertical offset from the center point
     */
    public GenericRegion(int radius, int height, int heightOffset) {
        this.radius = radius;
        this.height = height;
        this.heightOffset = heightOffset;
    }

    /**
     * Creates a specific region implementation around a center point.
     *
     * @param world The world in which to create the region
     * @param center The center point of the region
     * @return An AbstractRegion representing the specific region implementation
     */
    protected abstract AbstractRegion createRegion(World world, BlockVector3 center);

    /**
     * Creates a region centered on a player's location.
     *
     * @param player The player whose location will be used as the center of the region
     * @return An AbstractRegion centered on the player's location
     */
    public AbstractRegion getRegion(Player player) {
        return createRegion(BukkitAdapter.adapt(player.getWorld()), BlockVector3.at(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ));
    }
}
