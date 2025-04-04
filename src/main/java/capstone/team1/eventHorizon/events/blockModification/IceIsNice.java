package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCuboidRegion;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCylindricalRegion;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;

public class IceIsNice extends BaseBlockModification
{
    public IceIsNice()
    {
        super(EventClassification.NEUTRAL, "iceIsNice", new GenericCylindricalRegion(10,3,0), Material.PACKED_ICE, EventHorizon.getBlockMasks().getGroundBlocks(), false);
    }
    public void execute(){
        super.execute();
    }
    @Override
    public void terminate(){
        //do nothing
    }
}
