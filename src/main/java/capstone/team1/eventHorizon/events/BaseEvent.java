package capstone.team1.eventHorizon.events;

import org.bukkit.entity.Player;

public abstract class BaseEvent
{
    private final EventClassification classification;

    public BaseEvent(EventClassification classification)
    {
        this.classification = classification;
    }

    public EventClassification getClassification()
    {
        return classification;
    }

    public abstract void execute();
}
