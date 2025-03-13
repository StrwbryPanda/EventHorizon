package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.TournamentTimer;
import capstone.team1.eventHorizon.Util;
import capstone.team1.eventHorizon.events.EventScheduler;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandStart
{
    public static int time = 60; //tournament time variable

    public static void run(CommandSender sender) {
        EventHorizon.tournamentTimer.start(time);
    }
}
