package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.TournamentTimer;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandResume
{

    public static void run(CommandSender sender, EventHorizon plugin) {
        if (CommandPause.timeReamining > 0 && !TournamentTimer.isRunning) {
            TournamentTimer.totalTime = CommandPause.timeReamining;
            CommandsManager.tournamentTimer = new TournamentTimer(plugin);
            CommandsManager.tournamentTimer.startTimer();
            TournamentTimer.isRunning = true;
            sender.sendRichMessage("Resuming tournament timer");
        }
        //Timer already running
        else {
            sender.sendRichMessage("Tournament is already running");
        }

    }
}
