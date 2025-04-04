package capstone.team1.eventHorizon.events.utility.fawe.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.world.World;

public class GenericCylindricalRegion extends GenericRegion
{

    public GenericCylindricalRegion(int radius, int height, int heightOffset)
    {
        super(radius, height, heightOffset);
    }

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
