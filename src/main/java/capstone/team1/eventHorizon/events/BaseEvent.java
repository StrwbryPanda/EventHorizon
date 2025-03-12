package capstone.team1.eventHorizon.events;

import org.bukkit.entity.Player;

public abstract class BaseEvent
{
    public final String eventName;
    private final EventClassification classification;

    public BaseEvent(EventClassification classification, String eventName)
    {
        this.classification = classification;
        this.eventName = eventName;
    }

    public EventClassification getClassification()
    {
        return classification;
    }

    public abstract void execute();
    public EventClassification getEventClassification(BaseEvent event){
        return event.classification;
    }

    // TODO: Add a method to stop the event from executing
}
