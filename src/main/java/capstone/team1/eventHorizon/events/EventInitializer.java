package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.events.attributes.*;
import capstone.team1.eventHorizon.events.blockModification.IceIsNice;
import capstone.team1.eventHorizon.events.dropModification.BlockDropShuffle;
import capstone.team1.eventHorizon.events.dropModification.DoubleOrNothing;
import capstone.team1.eventHorizon.events.dropModification.MobDropShuffle;
import capstone.team1.eventHorizon.events.effects.*;
import capstone.team1.eventHorizon.events.gameRule.LifestealOnly;
import capstone.team1.eventHorizon.events.inventoryAdjustments.ButterFingers;
import capstone.team1.eventHorizon.events.inventoryAdjustments.FlightSchool;
import capstone.team1.eventHorizon.events.inventoryAdjustments.SpoiledFood;
import capstone.team1.eventHorizon.events.itemSpawn.Feast;
import capstone.team1.eventHorizon.events.itemSpawn.OreDropParty;
import capstone.team1.eventHorizon.events.mobSpawn.*;
import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the initialization, registration, and management of all events in the EventHorizon plugin.
 * This class maintains collections of registered events and their classifications (positive, negative, neutral).
 */
public class EventInitializer {
    private final HashMap<String, BaseEvent> registeredEvents = new HashMap<>();
    private final HashMap<EventClassification,List<BaseEvent>> enabledEvents = new HashMap<>();
    private final List<BaseEvent> posEvents = new ArrayList<>();
    private final List<BaseEvent> negEvents = new ArrayList<>();
    private final List<BaseEvent> neutralEvents = new ArrayList<>();

    /**
     * Initializes the event system by registering available events and setting up
     * the classification mappings for enabled events.
     */
    public EventInitializer() {
        registerAvailableEvents();
        reloadEvents();
        enabledEvents.put(EventClassification.POSITIVE, posEvents);
        enabledEvents.put(EventClassification.NEGATIVE, negEvents);
        enabledEvents.put(EventClassification.NEUTRAL, neutralEvents);
    }

    /**
     * Loads enabled events from the configuration file and adds them to their
     * respective classification lists.
     */
    private void loadEventsFromConfig() {
        List<String> enabledEventNames = Config.getEnabledEvents();
        Bukkit.getLogger().warning("Enabled events: " + enabledEvents);
        for (String eventName : enabledEventNames) {
            BaseEvent event = registeredEvents.get(eventName.toLowerCase());

            Bukkit.getLogger().warning("event class: " + event);
            MsgUtility.broadcast("event name: " + eventName);

            if (event != null) {
                enableEvent(event);
            }
        }
    }

    /**
     * Adds an event to its corresponding classification list.
     *
     * @param event The event to be enabled and classified
     */
    private void enableEvent(BaseEvent event) {
        switch (event.getEventClassification(event)) {
            case POSITIVE -> posEvents.add(event);
            case NEGATIVE -> negEvents.add(event);
            case NEUTRAL -> neutralEvents.add(event);
        }
    }

    /**
     * Registers all available events in the plugin by adding them to the registeredEvents map.
     * Events are organized by their categories: attributes, block modification, effects,
     * inventory adjustments, item spawn, and mob spawn.
     */
    private void registerAvailableEvents() {
        // Attribute events
        registeredEvents.put("fasting", new Fasting());
        registeredEvents.put("growthspurt", new GrowthSpurt());
        registeredEvents.put("halfaheart", new HalfAHeart());
        registeredEvents.put("honeyishrunkthekids", new HoneyIShrunkTheKids());
        registeredEvents.put("lifestealonly", new LifestealOnly());
        registeredEvents.put("zerogravity", new ZeroGravity());

        // Block modification events
        registeredEvents.put("iceisnice", new IceIsNice());

        // Drop modification events
        registeredEvents.put("blockdropshuffle", new BlockDropShuffle());
        registeredEvents.put("doubleornothing", new DoubleOrNothing());
        registeredEvents.put("mobdropshuffle", new MobDropShuffle());

        // Effect events
        registeredEvents.put("foodcoma", new FoodComa());
        registeredEvents.put("gottagofast", new GottaGoFast());
        registeredEvents.put("overmine", new Overmine());
        registeredEvents.put("secondwind", new SecondWind());
        registeredEvents.put("youretooslow", new YoureTooSlow());

        // Inventory adjustment events
        registeredEvents.put("butterfingers", new ButterFingers());
        registeredEvents.put("flightschool", new FlightSchool());
        registeredEvents.put("spoiledfood", new SpoiledFood());

        // Item spawn events
        registeredEvents.put("feast", new Feast());
        registeredEvents.put("oredropparty", new OreDropParty());

        // Mob spawn events
        registeredEvents.put("chickenflock", new ChickenFlock());
        registeredEvents.put("cowherd", new CowHerd());
        registeredEvents.put("dropcreeper", new DropCreeper());
        registeredEvents.put("randommobspawn", new RandomMobSpawn());
        registeredEvents.put("wolfpack", new WolfPack());
        registeredEvents.put("zombiehorde", new ZombieHorde());
        registeredEvents.put("zombieinvasion", new ZombieInvasion());
    }

    /**
     * Reloads all events by clearing the current classifications and
     * reloading from the configuration file.
     */
    public void reloadEvents() {
        posEvents.clear();
        negEvents.clear();
        neutralEvents.clear();
        loadEventsFromConfig();
    }

    /**
     * Gets all registered events regardless of their enabled status.
     *
     * @return A HashMap containing all registered events with their names as keys
     */
    public HashMap<String, BaseEvent> getRegisteredEvents() {
        return registeredEvents;
    }

    /**
     * Gets all currently enabled events organized by their classification.
     *
     * @return A HashMap containing lists of events grouped by their classification
     */
    public HashMap<EventClassification, List<BaseEvent>> getEnabledEvents() {
        return enabledEvents;
    }
}