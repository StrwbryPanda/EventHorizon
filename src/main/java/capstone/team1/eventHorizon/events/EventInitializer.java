package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.events.attributes.Fasting;
import capstone.team1.eventHorizon.events.attributes.GrowthSpurt;
import capstone.team1.eventHorizon.events.attributes.HoneyIShrunkTheKids;
import capstone.team1.eventHorizon.events.attributes.LifestealOnly;
import capstone.team1.eventHorizon.events.attributes.ZeroGravity;
import capstone.team1.eventHorizon.events.blockModification.IceIsNice;
import capstone.team1.eventHorizon.events.effects.FoodComa;
import capstone.team1.eventHorizon.events.effects.GottaGoFast;
import capstone.team1.eventHorizon.events.effects.Overmine;
import capstone.team1.eventHorizon.events.effects.SecondWind;
import capstone.team1.eventHorizon.events.effects.YoureTooSlow;
import capstone.team1.eventHorizon.events.mobSpawn.ChickenFlock;
import capstone.team1.eventHorizon.events.mobSpawn.CowHerd;
import capstone.team1.eventHorizon.events.mobSpawn.DropCreeper;
import capstone.team1.eventHorizon.events.mobSpawn.RandomMobSpawn;
import capstone.team1.eventHorizon.events.mobSpawn.ZombieHorde;
import capstone.team1.eventHorizon.events.mobSpawn.ZombieInvasion;
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

    private void loadEventsFromConfig() {
        List<String> enabledEventNames = Config.getEnabledEvents();
        Bukkit.getLogger().warning("Enabled events: " + enabledEvents);
        for (String eventName : enabledEventNames) {
            BaseEvent event = registeredEvents.get(eventName.toLowerCase());

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
        // Attribute events
// Attribute events
        registeredEvents.put("fasting", new Fasting());
        registeredEvents.put("growthspurt", new GrowthSpurt());
        registeredEvents.put("honeyishrunkthekids", new HoneyIShrunkTheKids());
        registeredEvents.put("lifestealonly", new LifestealOnly());
        registeredEvents.put("zerogravity", new ZeroGravity());

// Block modification events
        registeredEvents.put("iceisnice", new IceIsNice());

// Effect events
        registeredEvents.put("foodcoma", new FoodComa());
        registeredEvents.put("gottagofast", new GottaGoFast());
        registeredEvents.put("overmine", new Overmine());
        registeredEvents.put("secondwind", new SecondWind());
        registeredEvents.put("youretooslow", new YoureTooSlow());

// Mob spawn events
        registeredEvents.put("chickenflock", new ChickenFlock());
        registeredEvents.put("cowherd", new CowHerd());
        registeredEvents.put("dropcreeper", new DropCreeper());
        registeredEvents.put("randommobspawn", new RandomMobSpawn());
        registeredEvents.put("wolfpack", new WolfPack());
        registeredEvents.put("zombiehorde", new ZombieHorde());
        registeredEvents.put("zombieinvasion", new ZombieInvasion());
    }


    public void reloadEvents() {
        posEvents.clear();
        negEvents.clear();
        neutralEvents.clear();
        loadEventsFromConfig();
    }

    public HashMap<String, BaseEvent> getRegisteredEvents()
    {
        return registeredEvents;
    }

    public HashMap<EventClassification, List<BaseEvent>> getEnabledEvents()
    {
        return enabledEvents;
    }
}
