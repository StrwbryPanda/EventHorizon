package capstone.team1.eventHorizon.events;

public abstract class BaseEvent
{
    public final String eventName;
    private final EventClassification classification;

    public BaseEvent(EventClassification classification, String eventName)
    {
        this.classification = classification;
        this.eventName = eventName;
    }

    public abstract void execute();
    public abstract void terminate();

    public EventClassification getClassification()
    {
        return classification;
    }
    public EventClassification getEventClassification(BaseEvent event){
        return event.classification;
    }

    public String getName()
    {
        return eventName;
    }
}
