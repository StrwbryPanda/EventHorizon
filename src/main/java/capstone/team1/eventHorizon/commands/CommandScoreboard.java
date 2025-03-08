package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import org.bukkit.command.CommandSender;

//allows the user to toggle the scoreboard GUI
public class CommandScoreboard
{
    public static boolean isScoreboardOn = EventHorizon.plugin.isScoreboardOn;


    public static void run(CommandSender sender, EventHorizon plugin){

        isScoreboardOn = !isScoreboardOn;

        if(isScoreboardOn)
            sender.sendRichMessage("Scoreboard turn on");
        else
            sender.sendRichMessage("Scoreboard turned off");

    }
}
