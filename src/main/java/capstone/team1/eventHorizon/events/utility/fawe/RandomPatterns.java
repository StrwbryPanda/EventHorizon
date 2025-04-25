package capstone.team1.eventHorizon.events.utility.fawe;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.AbstractPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Material;

public class RandomPatterns
{
    private static Pattern netherPattern;

    private void initializeNetherPattern() {
        RandomPattern pattern = new RandomPattern();
        pattern.add(BukkitAdapter.asBlockType(Material.NETHERRACK), 0.5);
        pattern.add(BukkitAdapter.asBlockType(Material.SOUL_SAND), 0.5);
        netherPattern = pattern;
    }
    public Pattern getNetherPattern() {
        if (netherPattern == null) {
            initializeNetherPattern();
        }
        return netherPattern;
    }


}
