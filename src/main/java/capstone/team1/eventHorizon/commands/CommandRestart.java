package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.TournamentTimer;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandRestart
{
    public static int time = CommandStart.time; //tournament time variable


    public static void run(CommandSender sender, EventHorizon plugin) {
        if (CommandsManager.tournamentTimer != null && !CommandEnd.isEnded) { //can only be used if there is already an existing timer and it has not been ended
            CommandsManager.tournamentTimer.cancel();
            CommandEnd.isEnded = false;
            TournamentTimer.totalTime = time;
            CommandsManager.tournamentTimer = new TournamentTimer(plugin);
            CommandsManager.tournamentTimer.startTimer();
            TournamentTimer.isRunning = true;
            sender.sendRichMessage("Timer restarted");
        }
        else {
            sender.sendRichMessage("Unable to restart timer");
        }

    }
}
