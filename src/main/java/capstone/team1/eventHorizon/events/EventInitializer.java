package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.utility.MsgUtil;
import capstone.team1.eventHorizon.events.mobSpawn.WolfPack;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//class that handles event loading and registration
public class EventInitializer
{
    private final HashMap<String, BaseEvent> registeredEvents = new HashMap<>();
    private final HashMap<EventClassification,List<BaseEvent>> enabledEvents = new HashMap<>();
    private final List<BaseEvent> posEvents = new ArrayList<>();
    private final List<BaseEvent> negEvents = new ArrayList<>();
    private final List<BaseEvent> neutralEvents = new ArrayList<>();

    public EventInitializer()
    {
        registerAvailableEvents();
        reloadEvents();
        enabledEvents.put(EventClassification.POSITIVE, posEvents);
        enabledEvents.put(EventClassification.NEGATIVE, negEvents);
        enabledEvents.put(EventClassification.NEUTRAL, neutralEvents);
    }

    public Map<EventClassification, List<BaseEvent>> getEnabledEvents() {
        return enabledEvents;
    }

    private void loadEventsFromConfig() {
        List<String> enabledEventNames = Config.getEnabledEvents();
        Bukkit.getLogger().warning("Enabled events: " + enabledEvents);
        for (String eventName : enabledEventNames) {
            BaseEvent event = registeredEvents.get(eventName);

            Bukkit.getLogger().warning("event class: " + event);
            MsgUtil.broadcast("event name: " + eventName);

            if (event != null) {
                enableEvent(event);
            }
        }
    }

    private void enableEvent(BaseEvent event) {
        switch (event.getEventClassification(event)) {
            case POSITIVE -> posEvents.add(event);
            case NEGATIVE -> negEvents.add(event);
            case NEUTRAL -> neutralEvents.add(event);
        }
    }

    private void registerAvailableEvents() {
        registeredEvents.put("WolfPack", new WolfPack());
    }

    public void reloadEvents() {
        posEvents.clear();
        negEvents.clear();
        neutralEvents.clear();
        loadEventsFromConfig();
    }
}
