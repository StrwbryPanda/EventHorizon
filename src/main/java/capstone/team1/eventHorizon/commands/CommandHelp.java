package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandHelp {

    private static final Map<String, String> commands = new LinkedHashMap<>(); // Keeps order

     private static void loadCommands() {
        commands.put("start", "Starts the Event Horizon game mode timer.");
        commands.put("stop", "Stops the Event Horizon game mode timer.");
        commands.put("help", "Displays available Event Horizon commands.");
        commands.put("resume", "Resumes the Event Horizon game mode timer.");
    }

    public static void run(@NotNull CommandSender sender, EventHorizon plugin) {

        // Send help message asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                loadCommands();

                sender.sendMessage(Component.text("§aEvent Horizon Commands:"));

                for (String key : commands.keySet() ) {
                    sender.sendMessage((MiniMessage.miniMessage().deserialize("<hover:show_text:'<gray>" + commands.get(key) + "'><gray>/eventhorizon " + key)));
                }
            }
        }.runTaskAsynchronously(plugin);
    }

}


