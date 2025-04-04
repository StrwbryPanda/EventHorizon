package capstone.team1.eventHorizon.events.utility.fawe.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;

public class GenericCuboidRegion extends GenericRegion
{
    public GenericCuboidRegion(int radius, int height, int heightOffset)
    {
        super(radius, height, heightOffset);
    }

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
