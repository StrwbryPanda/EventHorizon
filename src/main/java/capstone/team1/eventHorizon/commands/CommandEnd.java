package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.TournamentTimer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

//command that stops the tournament timer
public class CommandEnd
{
public static boolean isEnded;

    public static void run(CommandSender sender) {
//        if (CommandsManager.tournamentTimer != null) { //only works if there is an existing timer
//            MiniMessage miniMessage = MiniMessage.miniMessage();
//
//            // Confirmation message with clickable options
//            String message = """
//                Are you sure you want to cancel?
//                <click:run_command:'/eventhorizon yes'><green>[Yes]</green></click>
//                <click:run_command:'/eventhorizon no'><red>[No]</red></click>
//                """;
//
//            Component component = miniMessage.deserialize(message);
//            sender.sendMessage(component);
//
//
//        } else {
//            sender.sendRichMessage("Timer is not running");
//        }
//    }
//
//    public static void yes(CommandSender sender) {
//        CommandsManager.tournamentTimer.cancel();
//        TournamentTimer.isRunning = false;
//        isEnded = true;
//        sender.sendRichMessage("Timer canceled");
//    }
//
//    public static void no(CommandSender sender) {
//        sender.sendRichMessage("The timer was NOT canceled");
//    }
}}
