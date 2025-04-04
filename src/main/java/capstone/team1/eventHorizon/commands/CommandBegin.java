package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandBegin
{
    public static void run(CommandSender sender) {
        MsgUtility.message(sender, EventHorizon.scheduler.start(Config.getTournamentTimer()) ? "The tournament has started" : "<red>ERROR: Tournament already started");
    }

}
