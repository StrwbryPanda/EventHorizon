package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.mobSpawn.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandTrigger {

    // Store all available event types
    private enum EventType {
        WOLF_PACK(WolfPack.class),
        CUSTOM(null);

        private final Class<? extends BaseMobSpawn> eventClass;

        EventType(Class<? extends BaseMobSpawn> eventClass) {
            this.eventClass = eventClass;
        }

        public Class<? extends BaseMobSpawn> getEventClass() {
            return eventClass;
        }
    }

    /**
     * Run the trigger command
     */
    public static void run(CommandSender sender, EventHorizon plugin, String[] args) {
        if (args.length < 1) {
            showUsage(sender);
            return;
        }

        String eventName = args[0].toUpperCase();
        EventType eventType;

        try {
            eventType = EventType.valueOf(eventName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("Unknown event type: " + args[0], NamedTextColor.RED));
            sender.sendMessage(Component.text("Available events: " +
                    Arrays.stream(EventType.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")), NamedTextColor.YELLOW));
            return;
        }

        // Default to all players if not specified
        Player targetPlayer = null;
        if (args.length >= 2 && !args[1].equalsIgnoreCase("all")) {
            targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                sender.sendMessage(Component.text("Player not found: " + args[1], NamedTextColor.RED));
                return;
            }
        }

        try {
            if (eventType == EventType.CUSTOM) {
                // Handle custom mob spawning
                if (args.length < 3) {
                    sender.sendMessage(Component.text("For custom events: /eventhorizon trigger CUSTOM [player|all] <mobType> [count] [maxRadius] [minRadius]", NamedTextColor.RED));
                    return;
                }

                EntityType mobType;
                try {
                    mobType = EntityType.valueOf(args[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("Invalid mob type: " + args[2], NamedTextColor.RED));
                    return;
                }

                int count = (args.length >= 4) ? parsePositiveInt(args[3], 5) : 5;
                int maxRadius = (args.length >= 5) ? parsePositiveInt(args[4], 15) : 15;
                int minRadius = (args.length >= 6) ? parsePositiveInt(args[5], 5) : 5;

                BaseMobSpawn spawnEvent = new BaseMobSpawn(plugin, mobType) {
                    @Override
                    protected void onMobSpawned(org.bukkit.entity.Entity entity, Player player) {
                        // This is where you could add custom behavior for the custom spawn
                    }
                };

                spawnEvent.setMobCount(count)
                        .setMaxSpawnRadius(maxRadius)
                        .setMinSpawnRadius(minRadius);

                spawnMobs(sender, spawnEvent, targetPlayer);
            } else if (eventType.getEventClass() != null) {
                // Create an instance of the specified event class
                BaseMobSpawn spawnEvent = eventType.getEventClass()
                        .getConstructor(org.bukkit.plugin.Plugin.class)
                        .newInstance(plugin);

                // Parse optional parameters if provided (count, maxRadius, minRadius)
                if (args.length >= 3) {
                    spawnEvent.setMobCount(parsePositiveInt(args[2], spawnEvent.mobCount));
                }
                if (args.length >= 4) {
                    spawnEvent.setMaxSpawnRadius(parsePositiveInt(args[3], spawnEvent.maxSpawnRadius));
                }
                if (args.length >= 5) {
                    spawnEvent.setMinSpawnRadius(parsePositiveInt(args[4], spawnEvent.minSpawnRadius));
                }

                spawnMobs(sender, spawnEvent, targetPlayer);
            } else {
                sender.sendMessage(Component.text("Event type not implemented yet: " + eventType, NamedTextColor.RED));
            }
        } catch (Exception e) {
            sender.sendMessage(Component.text("Error executing event: " + e.getMessage(), NamedTextColor.RED));
            e.printStackTrace();
        }
    }

    private static void spawnMobs(CommandSender sender, BaseMobSpawn spawnEvent, Player targetPlayer) {
        int spawned;
        if (targetPlayer != null) {
            spawned = spawnEvent.spawnForPlayer(targetPlayer).size();
            sender.sendMessage(Component.text("Spawned " + spawned + " mobs for player " + targetPlayer.getName(), NamedTextColor.GREEN));
        } else {
            spawned = spawnEvent.spawnForAllPlayers();
            sender.sendMessage(Component.text("Spawned a total of " + spawned + " mobs for all online players", NamedTextColor.GREEN));
        }
    }

    private static int parsePositiveInt(String input, int defaultValue) {
        try {
            int value = Integer.parseInt(input);
            return Math.max(1, value); // Ensure value is at least 1
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static void showUsage(CommandSender sender) {
        sender.sendMessage(Component.text("Trigger Command Usage:", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/eventhorizon trigger <eventType> [player|all] [options]", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("Available event types: " +
                Arrays.stream(EventType.values())
                        .map(Enum::name)
                        .map(String::toLowerCase)
                        .collect(Collectors.joining(", ")), NamedTextColor.AQUA));
    }

    /**
     * Get tab completions for the trigger command
     */
    public static List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // First argument - event type
            String currentArg = args[0].toUpperCase();
            for (EventType type : EventType.values()) {
                if (type.name().startsWith(currentArg)) {
                    completions.add(type.name().toLowerCase());
                }
            }
        } else if (args.length == 2) {
            // Second argument - player name or "all"
            String currentArg = args[1].toLowerCase();
            if ("all".startsWith(currentArg)) {
                completions.add("all");
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(currentArg)) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("CUSTOM")) {
            // For custom events, third arg is mob type
            String currentArg = args[2].toUpperCase();
            for (EntityType type : EntityType.values()) {
                if (type.isSpawnable() && type.isAlive() && type.name().startsWith(currentArg)) {
                    completions.add(type.name().toLowerCase());
                }
            }
        }

        return completions;
    }
}