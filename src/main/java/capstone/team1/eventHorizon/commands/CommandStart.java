package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.TournamentTimer;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandStart
{
    public static int time = 60; //tournament time variable


    public static void run(CommandSender sender, EventHorizon plugin) {
        if (CommandsManager.tournamentTimer == null || CommandsManager.tournamentTimer.isCancelled()) {
            TournamentTimer.totalTime = time;
            CommandsManager.tournamentTimer = new TournamentTimer(plugin);
            CommandsManager.tournamentTimer.startTimer();
            TournamentTimer.isRunning = true;
            sender.sendRichMessage("Starting tournament timer");
        }
        //Timer already running
        else {
            sender.sendRichMessage("Tournament is already running");
        }

    }
}
