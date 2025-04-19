package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.EventHorizon;

/**
 * Abstract base class for all events in the EventHorizon plugin.
 * Provides common functionality and structure for event implementation.
 */
public abstract class BaseEvent
{
    /** The name of the event */
    public final String eventName;
    /** The classification (POSITIVE, NEGATIVE, NEUTRAL) of the event */
    private final EventClassification classification;

    /**
     * Constructs a new event with the specified classification and name.
     *
     * @param classification The event's classification (POSITIVE, NEGATIVE, NEUTRAL)
     * @param eventName The name of the event
     */
    public BaseEvent(EventClassification classification, String eventName)
    {
        this.classification = classification;
        this.eventName = eventName;
    }

    /**
     * Executes the event's main functionality.
     * Must be implemented by concrete event classes.
     */
    public abstract void execute();

    /**
     * Terminates the event's effects.
     * Must be implemented by concrete event classes.
     */
    public abstract void terminate();

    /**
     * Runs the event by terminating any current event and executing this one.
     * This method handles the event lifecycle management.
     */
    public void run(){
        BaseEvent currentEvent = EventHorizon.getEventManager().getCurrentEvent();
        if(currentEvent != null){
            currentEvent.terminate();
        }
        EventHorizon.getEventManager().setCurrentEvent(this);
        this.execute();
    }

    /**
     * Gets the classification of this event.
     *
     * @return The event's classification
     */
    public EventClassification getClassification()
    {
        return classification;
    }

    /**
     * Gets the classification of the specified event.
     *
     * @param event The event to get the classification from
     * @return The event's classification
     */
    public EventClassification getEventClassification(BaseEvent event){
        return event.classification;
    }

    /**
     * Gets the name of this event.
     *
     * @return The event's name
     */
    public String getName()
    {
        return eventName;
    }
}