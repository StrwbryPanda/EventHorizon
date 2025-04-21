package capstone.team1.eventHorizon.events.utility.fawe.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.world.World;

/**
 * A specific implementation of GenericRegion that creates cylindrical regions.
 * This class defines regions in a cylindrical shape with customizable radius and height.
 */
public class GenericCylindricalRegion extends GenericRegion
{
    /**
     * Constructs a new cylindrical region with specified dimensions.
     *
     * @param radius The radius of the cylinder
     * @param height The height of the cylinder
     * @param heightOffset The vertical offset from the center point
     */
    public GenericCylindricalRegion(int radius, int height, int heightOffset)
    {
        super(radius, height, heightOffset);
    }
    /**
     * Creates a cylindrical region around a center point.
     * The cylinder extends from (center.y - height + heightOffset) to (center.y - 1 + heightOffset).
     *
     * @param world The world in which to create the region
     * @param center The center point of the cylindrical region
     * @return A CylinderRegion instance representing the cylindrical area
     */
    @Override
    protected AbstractRegion createRegion(World world, BlockVector3 center)
    {
        return new CylinderRegion(
                world,
                center,
                new Vector2(radius, radius), //radius
                center.y() - height + heightOffset, //start height blocks below player's feet
                center.y() - 1 + heightOffset //end at player's feet
        );
    }
}
