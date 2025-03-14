package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.Util;
import org.bukkit.command.CommandSender;

//command that starts the tournament timer
public class CommandPause
{

    public static int timeReamining;

    public static void run(CommandSender sender) {
        Util.message(sender, EventHorizon.gameTimer.pause() ? "Tournament has been paused" : "<red>ERROR: Cannot pause tournament");
    }
}
