package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.utility.MsgUtility;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.*;

/**
 * Manages the triggering and execution of events in the EventHorizon plugin.
 * This class handles random event selection based on weights, specific event triggering,
 * and maintains the current active event state.
 */
public class EventManager {
    /** Weight for positive events loaded from config */
    private double posWeight;
    /** Weight for negative events loaded from config */
    private double negWeight;
    /** Weight for neutral events loaded from config */
    private double neutralWeight;
    /** Random number generator for event selection */
    private final Random random = new Random();
    /** Currently active event */
    BaseEvent currentEvent;

    /**
     * Constructs a new EventManager and initializes event weights from config.
     */
    public EventManager() {
        this.posWeight = Config.getPosWeight();
        this.negWeight = Config.getNegWeight();
        this.neutralWeight = Config.getNeutralWeight();
        loadWeightsFromConfig();
    }

    /**
     * Gets the currently active event.
     *
     * @return The current BaseEvent instance, or null if no event is active
     */
    public BaseEvent getCurrentEvent() {
        return currentEvent;
    }

    /**
     * Sets the current active event.
     *
     * @param event The BaseEvent to set as current
     */
    public void setCurrentEvent(BaseEvent event) {
        currentEvent = event;
    }

    /**
     * Triggers a random event based on the configured weights for different event classifications.
     * The event is selected using weighted probability and executed on the next server tick.
     * Broadcasts the selected event name and displays it as a title and action bar message.
     */
    public void triggerEventBtWeight() {
        Bukkit.getLogger().info("Triggering event...");

        List<EventClassification> items = List.of(EventClassification.POSITIVE, EventClassification.NEGATIVE, EventClassification.NEUTRAL);
        List<Double> weights = List.of(posWeight, negWeight, neutralWeight);
        double totalWeight = 0.0;
        for (Double weight : weights) {
            totalWeight += weight;
        }

        double randomNumber = random.nextDouble() * totalWeight;

        double cumulativeWeight = 0.0;
        for (int i = 0; i < items.size(); i++) {
            cumulativeWeight += weights.get(i);

            if (randomNumber < cumulativeWeight) {
                EventClassification eventClassification = items.get(i);
                List<BaseEvent> selectedEvents = EventHorizon.getEventInitializer().getEnabledEvents().get(eventClassification);
                BaseEvent selectedEvent = selectedEvents.get(random.nextInt(selectedEvents.size()));

                MsgUtility.broadcast("Selected event: " + selectedEvent.getName());
                MsgUtility.showTitleWithDurations(Bukkit.getServer(), selectedEvent.getName());
                MsgUtility.actionBar(Bukkit.getServer(), selectedEvent.getName());
                Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), () -> {
                    MsgUtility.sound(Bukkit.getServer(), Sound.sound(Key.key("minecraft:block.note_block.bell"), Sound.Source.BLOCK, 1.0f, 1.0f));
                });
                Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> selectedEvent.run());
                return;
            }
        }
    }

    /**
     * Triggers a specific event by its name.
     *
     * @param eventName The name of the event to trigger (case-insensitive)
     */
    public void triggerEventByName(String eventName) {
        BaseEvent event = EventHorizon.getEventInitializer().getRegisteredEvents().get(eventName.toLowerCase());
        if (event != null) {
            Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> event.run());
        }
    }

    /**
     * Triggers a completely random event from all enabled events,
     * regardless of their classification or weights.
     */
    public void triggerRandomEvent() {
        List<BaseEvent> allEvents = new ArrayList<>();
        for (List<BaseEvent> events : EventHorizon.getEventInitializer().getEnabledEvents().values()) {
            allEvents.addAll(events);
        }
        BaseEvent randomEvent = allEvents.get(random.nextInt(allEvents.size()));
        Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> randomEvent.run());
    }

    /**
     * Loads event weights from the configuration file and logs the new values.
     */
    private void loadWeightsFromConfig() {
        this.posWeight = Config.getPosWeight();
        this.negWeight = Config.getNegWeight();
        this.neutralWeight = Config.getNeutralWeight();

        Bukkit.getLogger().info("Raw weights - Pos: " + this.posWeight +
                ", Neg: " + this.negWeight +
                ", Neutral: " + this.neutralWeight);
    }
}