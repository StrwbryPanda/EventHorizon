package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.TournamentTimer;
import org.bukkit.command.CommandSender;

public class CommandStart
{
    public static void run(CommandSender sender) {
        if (tournamentTimer == null || tournamentTimer.isCancelled()) {
            tournamentTimer = new TournamentTimer(this);
            tournamentTimer.startTimer();
            sender.sendRichMessage("Tournament timer started!");
            //Timer already running
        } else {
            sender.sendRichMessage("Tournament is already running!");
        }

        sender.sendRichMessage("Starting tournament timer");
    }
}
