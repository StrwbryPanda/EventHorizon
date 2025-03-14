package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.Utility.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.Utility.MsgUtil;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandBegin
{
    public static void run(CommandSender sender) {
        MsgUtil.message(sender, EventHorizon.scheduler.start(Config.getTournamentTimer()) ? "The tournament has started" : "<red>ERROR: Tournament already started");
    }

}
