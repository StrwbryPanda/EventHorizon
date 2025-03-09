package capstone.team1.eventHorizon.events;

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
    public EventClassification getEventClassification(BaseEvent event){
        return event.classification;
    }
}
