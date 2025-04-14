package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.bukkit.command.CommandSender;

//command that resumes a paused tournament timer
public class CommandResume
{

    public static void run(CommandSender sender) {
        MsgUtility.message(sender, EventHorizon.getScheduler().resume() ? "Tournament has resumed" : "<red>ERROR: Cannot resume tournament");
    }
}
