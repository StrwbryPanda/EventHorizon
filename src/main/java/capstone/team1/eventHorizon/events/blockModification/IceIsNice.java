package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.events.EventClassification;

public class IceIsNice extends BaseBlockModification
{
    public IceIsNice()
    {
        super(EventClassification.NEUTRAL, "iceIsNice");
    }
    public void execute(){
        super.execute();
    }
    @Override
    public void terminate(){
        //do nothing
    }
}
