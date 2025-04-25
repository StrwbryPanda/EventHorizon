package capstone.team1.eventHorizon.events.blockModification.subEvents;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.blockModification.BaseBlockModification;
import capstone.team1.eventHorizon.events.utility.fawe.BlockEditor;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCylindricalRegion;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class subWaterToLava extends BaseBlockModification
{
    public subWaterToLava()
    {
        super(EventClassification.NEGATIVE, "subWaterToLava", new GenericCylindricalRegion(100,400,200), Material.LAVA, Arrays.asList(BukkitAdapter.asBlockType(Material.WATER)), false);
    }
    public void execute(){
        super.execute(false);
    }
    @Override
    public void terminate(){
        BlockEditor.clearActiveEditSessions();
    }
}
