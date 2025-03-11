package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.mobSpawn.BaseMobSpawn;
import capstone.team1.eventHorizon.events.mobSpawn.WolfPack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class EventScheduler
{
    private final Map<String, Class<? extends BaseEvent>> registeredEventTypes = new HashMap<>();

    private final Random random = new Random();
    private final EventHorizon plugin;
    private final List<BaseEvent> posEvents = new ArrayList<>();
    private final List<BaseEvent> negEvents = new ArrayList<>();
    private final List<BaseEvent> neutralEvents = new ArrayList<>();

    private double posWeight;
    private double negWeight;
    private double neutralWeight;




    public EventScheduler(EventHorizon plugin)
    {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        this.posWeight = config.getDouble("event.posWeight");
        this.negWeight = config.getDouble("event.negWeight");
        this.neutralWeight = config.getDouble("event.neutralWeight");
    }

    public void triggerEvent()
    {
        Bukkit.getLogger().info("Triggering event...");
        double totalWeight = posWeight + negWeight + neutralWeight;
        double normalizedPosWeight = posWeight / totalWeight;
        double normalizedNegWeight = negWeight / totalWeight;
        double randomNumber = random.nextDouble();
        BaseEvent selectedEvent = null;

        if (randomNumber < normalizedPosWeight) {
            if (!posEvents.isEmpty()) {
                selectedEvent = posEvents.get(random.nextInt(posEvents.size()));
            }
        } else if (randomNumber < normalizedPosWeight + normalizedNegWeight) {
            if (!negEvents.isEmpty()) {
                selectedEvent = negEvents.get(random.nextInt(negEvents.size()));
            }
        } else {
            if (!neutralEvents.isEmpty()) {
                selectedEvent = neutralEvents.get(random.nextInt(neutralEvents.size()));
            }
        }

        if (selectedEvent != null) {
            selectedEvent.execute();
        } else {
            Bukkit.getLogger().warning("No events available in the selected category!");
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
        List<String> enabledEvents = Config.getEnabledEvents();

        for (String eventName : enabledEvents) {
            Class<? extends BaseEvent> eventClass = registeredEventTypes.get(eventName);
            if (eventClass != null) {
                try {
                    BaseEvent event = eventClass.getDeclaredConstructor(EventHorizon.class)
                            .newInstance(plugin);
                    registerEvent(event);
                    Bukkit.getLogger().info("Loaded event: " + eventName);
                } catch (Exception e) {
                    Bukkit.getLogger().warning("Failed to load event: " + eventName);
                }
            }
        }
    }

    private void loadWeightsFromConfig() {
        this.posWeight = Config.getPosWeight();
        this.negWeight = Config.getNegWeight();
        this.neutralWeight = Config.getNeutralWeight();
    }
}