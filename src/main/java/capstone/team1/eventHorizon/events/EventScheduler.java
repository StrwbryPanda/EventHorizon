package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.mobSpawn.BaseMobSpawn;
import capstone.team1.eventHorizon.events.mobSpawn.WolfPack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class EventScheduler {
    private final Map<EventClassification, List<EventType>> eventsByType;
    private final Random random = new Random();
    private final EventHorizon plugin;
    private final double posWeight;
    private final double negWeight;
    private final double neutralWeight;

    public enum EventClassification {
        POSITIVE,
        NEGATIVE,
        NEUTRAL
    }

    public enum EventType {
        WOLF_PACK(WolfPack.class, EventClassification.NEGATIVE);
        // Add more events here

        private final Class<? extends BaseMobSpawn> eventClass;
        private final EventClassification classification;

        EventType(Class<? extends BaseMobSpawn> eventClass, EventClassification classification) {
            this.eventClass = eventClass;
            this.classification = classification;
        }

        public Class<? extends BaseMobSpawn> getEventClass() {
            return eventClass;
        }

        public EventClassification getClassification() {
            return classification;
        }
    }

    public EventScheduler(EventHorizon plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();

        this.posWeight = config.getDouble("event.posWeight", 0.2);
        this.negWeight = config.getDouble("event.negWeight", 0.2);
        this.neutralWeight = config.getDouble("event.neutralWeight", 0.6);

        this.eventsByType = new EnumMap<>(EventClassification.class);
        for (EventClassification classification : EventClassification.values()) {
            eventsByType.put(classification, new ArrayList<>());
        }

        // Categorize events
        for (EventType event : EventType.values()) {
            if (event.getEventClass() != null) {
                eventsByType.get(event.getClassification()).add(event);
            }
        }
    }

    public void triggerEvent() {
        double randomNumber = random.nextDouble();
        EventClassification classification;

        if (randomNumber < posWeight) {
            classification = EventClassification.POSITIVE;
        } else if (randomNumber < posWeight + negWeight) {
            classification = EventClassification.NEGATIVE;
        } else {
            classification = EventClassification.NEUTRAL;
        }

        List<EventType> possibleEvents = eventsByType.get(classification);
        if (possibleEvents.isEmpty()) {
            Bukkit.getLogger().warning("No events available for classification: " + classification);
            return;
        }

        EventType selectedEvent = possibleEvents.get(random.nextInt(possibleEvents.size()));
        try {
            BaseMobSpawn event = selectedEvent.getEventClass()
                    .getConstructor(org.bukkit.plugin.Plugin.class)
                    .newInstance(plugin);
            event.spawnForAllPlayers();

            Bukkit.broadcastMessage(ChatColor.GREEN + "Triggered event: " + selectedEvent.name());
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to trigger event: " + e.getMessage());
        }
    }
}