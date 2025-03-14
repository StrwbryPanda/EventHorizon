package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.effects.*;
import capstone.team1.eventHorizon.events.mobSpawn.BaseMobSpawn;
import capstone.team1.eventHorizon.events.mobSpawn.WolfPack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandTrigger {
    private static final Random random = new Random();
    private static EventHorizon plugin;

    ///  DELETE THIS
    // Create a map to store event name to creator function mapping
    private static final Map<String, EventCreator> EVENT_MAP = new HashMap<>();

    // Interface for event creation functions
    @FunctionalInterface
    private interface EventCreator {
        BaseEvent create(EventHorizon plugin);
    }

    // Static initializer to populate the event map
    static {
        EVENT_MAP.put("wolfpack", plugin -> new WolfPack());
        // Add more events here as needed
        EVENT_MAP.put("gottagofast", plugin -> new GottaGoFast());
        EVENT_MAP.put("overmine", plugin -> new Overmine());
        EVENT_MAP.put("youretooslow", plugin -> new YoureTooSlow());
        EVENT_MAP.put("foodcoma", plugin -> new FoodComa());
        EVENT_MAP.put("asecondwind", plugin -> new ASecondWind());
        // EVENT_MAP.put("zombiehorde", plugin -> new ZombieHorde(plugin));
    }

    // Lists to categorize events by classification
    private static final List<String> POSITIVE_EVENTS = new ArrayList<>();
    private static final List<String> NEGATIVE_EVENTS = new ArrayList<>();
    private static final List<String> NEUTRAL_EVENTS = new ArrayList<>();

    // Static initializer to categorize events
    static {
        // This needs to be customized based on your actual events
        NEGATIVE_EVENTS.add("wolfpack");
        // POSITIVE_EVENTS.add("diamondrain");
        // NEUTRAL_EVENTS.add("foggyweather");
        POSITIVE_EVENTS.add("gottagofast");
        POSITIVE_EVENTS.add("overmine");
        POSITIVE_EVENTS.add("asecondwind");
        NEGATIVE_EVENTS.add("youretooslow");
        POSITIVE_EVENTS.add("foodcoma");
    }

    // List of all available event names (including special commands)
    private static final List<String> AVAILABLE_EVENTS = new ArrayList<>(EVENT_MAP.keySet());

    static {
        // Add special command options
        AVAILABLE_EVENTS.add("random"); // COOL TO HAVE THIS
        AVAILABLE_EVENTS.add("positive");
        AVAILABLE_EVENTS.add("negative");
        AVAILABLE_EVENTS.add("neutral");
    }

    /// DELETE UP TO THIS

    /**
     * Executes the trigger command
     * @param sender The command sender
     * @param args Additional arguments (event name)
     */
    public static void run(CommandSender sender, String[] args) {
        plugin = EventHorizon.plugin;
        // Check if sender has permission
        if (!sender.hasPermission("eventhorizon.trigger")) {
            sender.sendRichMessage("<red>You don't have permission to trigger events!");
            return;
        }

        // Check if args are present
        if (args.length == 0) {
            sender.sendRichMessage("<red>Please specify an event to trigger. Usage: /eventhorizon trigger <event>");
            sender.sendRichMessage("<yellow>Available events: " + String.join(", ", AVAILABLE_EVENTS));
            return;
        }

        String eventName = args[0].toLowerCase();
        boolean success = false;

        try {
            // Method 1: Direct instantiation and execution of specific events
            if (EVENT_MAP.containsKey(eventName)) { // Kylie's .getEventByName(eventName).execute();
                BaseEvent event = EVENT_MAP.get(eventName).create(plugin);
                event.execute(); // Check if this is null first

                if (event instanceof BaseMobSpawn) {
                    BaseMobSpawn mobEvent = (BaseMobSpawn) event;
                    int spawned = mobEvent.getLastSpawnCount();
                    sender.sendRichMessage("<green>Triggered " + eventName + " event! Spawned " + spawned + " " + mobEvent.mobType.toString() + " entities.");
                } else {
                    sender.sendRichMessage("<green>Triggered " + eventName + " event!");
                }
                success = true;
            }
            // Method 2: Random event
            else if (eventName.equals("random")) { // Kylie's .getRandomEvent.execute();
                triggerRandomEvent(plugin, sender);
                success = true;
            }
            // Method 3: Trigger events by classification
            else if (eventName.equals("positive")) {
                triggerEventByType(plugin, sender, POSITIVE_EVENTS, "positive"); // Kylie's .getRandomPositiveEvent.execute();
                success = true;
            }
            else if (eventName.equals("negative")) {
                triggerEventByType(plugin, sender, NEGATIVE_EVENTS, "negative"); // Kylie's .getRandomNegativeEvent.execute();
                success = true;
            }
            else if (eventName.equals("neutral")) {
                triggerEventByType(plugin, sender, NEUTRAL_EVENTS, "neutral"); // Kylie's .getRandomNeutralEvent.execute();
                success = true;
            }
            else {
                sender.sendRichMessage("<red>Unknown event: " + eventName);
                sender.sendRichMessage("<yellow>Available events: " + String.join(", ", AVAILABLE_EVENTS));
            }
        } catch (Exception e) {
            sender.sendRichMessage("<red>Error triggering event: " + e.getMessage());
            plugin.getLogger().warning("Error triggering event: " + e.getMessage());
        }

        if (success && sender instanceof Player) {
            // Log the action
            plugin.getLogger().info("Player " + sender.getName() + " triggered event: " + eventName);
        }
    }

    /**
     * Helper method to trigger a random event
     */
    private static void triggerRandomEvent(EventHorizon plugin, CommandSender sender) { // MOVE TO KYLIE'S
        if (EVENT_MAP.isEmpty()) {
            sender.sendRichMessage("<red>No events available to trigger!");
            return;
        }

        List<String> eventNames = new ArrayList<>(EVENT_MAP.keySet());
        String randomEvent = eventNames.get(random.nextInt(eventNames.size()));

        BaseEvent event = EVENT_MAP.get(randomEvent).create(plugin);
        event.execute();

        sender.sendRichMessage("<green>Triggered random event: " + randomEvent);
    }

    /**
     * Helper method to trigger a random event of a specific type
     */
    private static void triggerEventByType(EventHorizon plugin, CommandSender sender,
                                           List<String> eventList, String typeName) { // MOVE TO KYLIE'S
        if (eventList.isEmpty()) {
            sender.sendRichMessage("<red>No " + typeName + " events available to trigger!");
            return;
        }

        String randomEvent = eventList.get(random.nextInt(eventList.size()));

        BaseEvent event = EVENT_MAP.get(randomEvent).create(plugin);
        event.execute();

        sender.sendRichMessage("<green>Triggered " + typeName + " event: " + randomEvent);
    }

    /**
     * Provides tab completion suggestions for the trigger command
     * @param sender The command sender
     * @param args The current arguments
     * @return Collection of strings for tab completion
     */
    public static Collection<String> suggest(CommandSender sender, String[] args) {
        if (args.length == 1) {
            // Use Paper's StringUtil for more efficient partial matching
            return StringUtil.copyPartialMatches(args[0], AVAILABLE_EVENTS, new ArrayList<>());
        }

        return new ArrayList<>();
    }
}