package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.effects.*;
import capstone.team1.eventHorizon.events.mobSpawn.WolfPack;
import org.bukkit.Bukkit;

import java.util.*;

public class EventScheduler
{
    private final EventHorizon plugin;
    private final HashMap<String, BaseEvent> registeredEvents = new HashMap<>();
    private final HashMap<EventClassification,List<BaseEvent>> enabledEvents = new HashMap<>();
    private final List<BaseEvent> posEvents = new ArrayList<>();
    private final List<BaseEvent> negEvents = new ArrayList<>();
    private final List<BaseEvent> neutralEvents = new ArrayList<>();
    private double posWeight;
    private double negWeight;
    private double neutralWeight;
    private final Random random = new Random();





    public EventScheduler(EventHorizon plugin)
    {
        Bukkit.getLogger().warning("Neg event size: " + negEvents.size());
        this.plugin = plugin;
        this.posWeight = Config.getPosWeight();
        this.negWeight = Config.getNegWeight();
        this.neutralWeight = Config.getNeutralWeight();

        registeredEvents.put("WolfPack", new WolfPack());
        registeredEvents.put("GottaGoFast", new GottaGoFast());
        registeredEvents.put("Overmine", new Overmine());
        registeredEvents.put("YoureTooSlow", new YoureTooSlow());
        registeredEvents.put("FoodComa", new FoodComa());
        registeredEvents.put("aSecondWind", new ASecondWind());

        reloadEvents();
        enabledEvents.put(EventClassification.POSITIVE, posEvents);
        enabledEvents.put(EventClassification.NEGATIVE, negEvents);
        enabledEvents.put(EventClassification.NEUTRAL, neutralEvents);
    }

    public void triggerEvent() {
        Bukkit.getLogger().info("Triggering event...");


        BaseEvent selectedEvent = null;
        List<EventClassification> items = List.of(EventClassification.POSITIVE, EventClassification.NEGATIVE, EventClassification.NEUTRAL);
        List<Double> weights = List.of(posWeight, negWeight, neutralWeight);
        double totalWeight = 0.0;
        for (Double weight : weights) {
            totalWeight += weight;
        }

        Random random = new Random();
        double randomNumber = random.nextDouble() * totalWeight;
        Bukkit.getLogger().warning("Random number: " + randomNumber);
        Bukkit.getLogger().warning("total weight: " + totalWeight);


        double cumulativeWeight = 0.0;
        for (int i = 0; i < items.size(); i++) {
            cumulativeWeight += weights.get(i);
            Bukkit.getLogger().warning("cum weight: " + cumulativeWeight);

            if (randomNumber < cumulativeWeight) {
                EventClassification eventClassification = items.get(i);
                Bukkit.getLogger().warning("event classification:" + eventClassification);
                List<BaseEvent> selectedEvents = enabledEvents.get(eventClassification);
                Bukkit.getLogger().warning("selected events: " + selectedEvents);
                selectedEvent = selectedEvents.get(random.nextInt(selectedEvents.size()));
                selectedEvent.execute();
//                Bukkit.getLogger().warning("event" + selectedEvent.toString());
                return;
            }
        }


    }

    private void registerEvent(BaseEvent event) {
        switch (event.getEventClassification(event)) {
            case POSITIVE -> posEvents.add(event);
            case NEGATIVE -> negEvents.add(event);
            case NEUTRAL -> neutralEvents.add(event);
        }
    }

    public void reloadEvents() {
        posEvents.clear();
        negEvents.clear();
        neutralEvents.clear();
        loadWeightsFromConfig();
        loadEventsFromConfig();
    }

    private void loadEventsFromConfig() {
        List<String> enabledEventNames = Config.getEnabledEvents();
        Bukkit.getLogger().warning("Enabled events: " + enabledEvents);

        for (String eventName : enabledEventNames) {
            BaseEvent event = registeredEvents.get(eventName);

            Bukkit.getLogger().warning("event class: " + event);

            if (event != null) {
                registerEvent(event);
            }
        }
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