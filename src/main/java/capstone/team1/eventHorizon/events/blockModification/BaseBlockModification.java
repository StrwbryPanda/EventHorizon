package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;

public abstract class BaseBlockModification extends BaseEvent
{
    public BaseBlockModification(EventClassification classification)
    {
        super(classification);
    }
    public abstract void execute();
}
