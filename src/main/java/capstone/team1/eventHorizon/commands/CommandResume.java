package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.Util;
import org.bukkit.command.CommandSender;

//command that resumes a paused tournament timer
public class CommandResume
{

    public static void run(CommandSender sender) {
        Util.message(sender, EventHorizon.gameTimer.resume() ? "Tournament has resumed" : "<red>ERROR: Cannot resume tournament");
    }
}
