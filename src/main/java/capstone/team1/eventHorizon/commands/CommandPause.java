package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.TournamentTimer;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandPause
{

    public static int timeReamining;

    public static void run(CommandSender sender, EventHorizon plugin) {
        if (TournamentTimer.isRunning) {
            timeReamining = TournamentTimer.remainingTime;
            CommandsManager.tournamentTimer.cancel();
            TournamentTimer.isRunning = false;
            sender.sendRichMessage("Pausing tournament timer");
        }
        //Timer already running
        else {
            sender.sendRichMessage("Tournament is not running");
        }

    }
}
