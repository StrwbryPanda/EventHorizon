package capstone.team1.eventHorizon.events.utility.fawe.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class GenericRegion
{
    protected int radius;
    protected int height;
    protected int heightOffset;
    protected String replacementBlockId;
    protected Collection<BlockType> blockTypesToReplace;

    public GenericRegion(int radius, int height, int heightOffset) {
        this.radius = radius;
        this.height = height;
        this.heightOffset = heightOffset;
    }

    protected abstract AbstractRegion createRegion(World world, BlockVector3 center);

    public AbstractRegion getRegion(Player player) {
        return createRegion(BukkitAdapter.adapt(player.getWorld()), BlockVector3.at(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ));
    }
}
