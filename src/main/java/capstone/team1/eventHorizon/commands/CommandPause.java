package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandPause
{

    public static int timeReamining;

    public static void run(CommandSender sender) {
        MsgUtility.message(sender, EventHorizon.getScheduler().pause() ? "Tournament has been paused" : "<red>ERROR: Cannot pause tournament");
    }
}
