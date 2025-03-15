package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;

public abstract class BaseBlockModification extends BaseEvent
{
    public BaseBlockModification(EventClassification classification, String eventName)
    {
        super(classification, eventName);
    }
    public abstract void execute();
    public void terminate(){};

}
