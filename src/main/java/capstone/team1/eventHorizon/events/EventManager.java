package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.utility.MsgUtil;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;

import java.util.*;

//class that handles event triggering
public class EventManager
{
    private final EventInitializer eventInitializer;
    private double posWeight;
    private double negWeight;
    private double neutralWeight;
    private final Random random = new Random();

    public EventManager(EventInitializer eventInitializer)
    {
        this.eventInitializer = eventInitializer;
        this.posWeight = Config.getPosWeight();
        this.negWeight = Config.getNegWeight();
        this.neutralWeight = Config.getNeutralWeight();
        loadWeightsFromConfig();

    }

    //trigger random events based on the weights set in the config
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
                List<BaseEvent> selectedEvents = eventInitializer.getEnabledEvents().get(eventClassification);
//                MsgUtil.broadcast("Selected event classification: " + eventClassification);
//                MsgUtil.broadcast("Selected events: " + selectedEvents);
                BaseEvent selectedEvent = selectedEvents.get(random.nextInt(selectedEvents.size()));
                MsgUtil.broadcast("Selected event: " + selectedEvent.getName());
                Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> selectedEvent.execute()); //uses a lamba function to run the event on the main thread on the next available tick
                return;
            }
        }
    }

    //trigger a specific event by name
    public void triggerEventByName(String eventName) {
        BaseEvent event = eventInitializer.getRegisteredEvents().get(eventName.toLowerCase());
        if (event != null) {
            event.execute();
        }
    }

    //trigger a random event
    public void triggerRandomEvent() {
        List<BaseEvent> allEvents = new ArrayList<>();
        for (List<BaseEvent> events : eventInitializer.getEnabledEvents().values()) {
            allEvents.addAll(events);
        }
        BaseEvent randomEvent = allEvents.get(random.nextInt(allEvents.size()));
        randomEvent.execute();
    }


    private void loadWeightsFromConfig() {
        this.posWeight = Config.getPosWeight();
        this.negWeight = Config.getNegWeight();
        this.neutralWeight = Config.getNeutralWeight();

        Bukkit.getLogger().info("Raw weights - Pos: " + this.posWeight +
                ", Neg: " + this.negWeight +
                ", Neutral: " + this.neutralWeight);
    }
}