package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.utility.fawe.BlockEditor;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCuboidRegion;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCylindricalRegion;
import org.bukkit.Material;

public class GoldRush extends BaseBlockModification
{
    public GoldRush()
    {
        super(EventClassification.POSITIVE, "goldRush", new GenericCuboidRegion(50,400,200), Material.GOLD_ORE, EventHorizon.getBlockMasks().getUndergroundBlocks(), false);
    }
    public void execute(){
        super.execute();
    }
    @Override
    public void terminate(){
        super.terminate();
    }
}
