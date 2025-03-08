package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.TournamentTimer;
import capstone.team1.eventHorizon.events.EventScheduler;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandStart
{
    public static int time; //tournament time variable

    public static void run(CommandSender sender, EventHorizon plugin) {
        if (CommandsManager.tournamentTimer == null || CommandsManager.tournamentTimer.isCancelled()) {
            time = plugin.getConfig().getInt("tournament.time", Config.getTournamentTimer()); //use config value
            CommandEnd.isEnded = false;
            TournamentTimer.totalTime = time;
            CommandsManager.tournamentTimer = new TournamentTimer(plugin);
            CommandsManager.tournamentTimer.startTimer();
            TournamentTimer.isRunning = true;
            sender.sendRichMessage("Timer started");
        }
        //Timer already running
        else {
            sender.sendRichMessage("Timer is already running");
        }

    }
}
