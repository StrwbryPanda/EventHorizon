package capstone.team1.eventHorizon.events.utility.fawe.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;

/**
 * A specific implementation of GenericRegion that creates cuboid regions.
 * This class defines regions in a rectangular box shape with customizable dimensions.
 */
public class GenericCuboidRegion extends GenericRegion
{
    /**
     * Constructs a new cuboid region with specified dimensions.
     *
     * @param radius The radius of the cuboid (extends in x and z directions)
     * @param height The height of the cuboid
     * @param heightOffset The vertical offset from the center point
     */
    public GenericCuboidRegion(int radius, int height, int heightOffset)
    {
        super(radius, height, heightOffset);
    }

    /**
     * Creates a cuboid region around a center point.
     * The cuboid extends from (center - radius) to (center + radius) horizontally,
     * and from (center.y - height + heightOffset) to (center.y - 1 + heightOffset) vertically.
     *
     * @param world The world in which to create the region
     * @param center The center point of the cuboid region
     * @return A CuboidRegion instance representing the rectangular box area
     */
    @Override
    protected AbstractRegion createRegion(World world, BlockVector3 center)
    {
        return new CuboidRegion(
            world,
            center.subtract(radius, height - heightOffset, radius), //start height blocks below and expand outward
            center.add(radius, -1 + heightOffset, radius) //end at player's feet
        );
    }
}
