package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.TournamentTimer;
import org.bukkit.command.CommandSender;

//command that stops the tournament timer
public class CommandStop
{
    public static int timeReamining;

    public static void run(CommandSender sender, EventHorizon plugin) {
        if (CommandsManager.tournamentTimer != null && !CommandsManager.tournamentTimer.isCancelled() && TournamentTimer.isRunning) {
            timeReamining = TournamentTimer.remainingTime;
            CommandsManager.tournamentTimer.cancel();
            TournamentTimer.isRunning = false;
            sender.sendRichMessage("Stopping tournament timer");
        } else {
            sender.sendRichMessage("Tournament is not running");
        }
    }

}
