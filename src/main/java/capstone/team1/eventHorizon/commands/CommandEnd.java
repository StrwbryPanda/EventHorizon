package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.MsgUtil;
import org.bukkit.command.CommandSender;

//command that stops the tournament timer
public class CommandEnd
{
public static boolean isEnded;

    public static void run(CommandSender sender) {
        MsgUtil.message(sender, EventHorizon.scheduler.end() ? "Tournament has ended" : "<red>ERROR: No active tournament");
    }
}
