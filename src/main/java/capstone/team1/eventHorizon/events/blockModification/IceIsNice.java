package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Minecart;

public class IceIsNice extends BaseBlockModification
{
    public IceIsNice()
    {
        super(EventClassification.NEUTRAL, "iceIsNice", "cylinderAtFeet", 10, 3, "minecraft:packed_ice");
    }
    public void execute(){
        super.execute();
    }
    @Override
    public void terminate(){
        //do nothing
    }
}
