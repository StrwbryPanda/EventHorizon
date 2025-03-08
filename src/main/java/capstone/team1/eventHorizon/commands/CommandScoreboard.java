package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.DisplaySlot;

import static capstone.team1.eventHorizon.ScoreboardManager.objective;

//allows the user to toggle the scoreboard GUI
public class CommandScoreboard
{
    public static boolean isScoreboardOn = true;

    public static void run(CommandSender sender, EventHorizon plugin){
        isScoreboardOn = !isScoreboardOn;

        if(isScoreboardOn)
            sender.sendRichMessage("Scoreboard turn on");
        else
            sender.sendRichMessage("Scoreboard turned off");

    }
}
