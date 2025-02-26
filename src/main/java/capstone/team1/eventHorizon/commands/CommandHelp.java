package capstone.team1.eventHorizon.commands;

import org.bukkit.command.CommandSender;

//command that displays all commands available to the player
//the commands can be hovered over to display a detailed description of the command
public class CommandHelp
{
    public static void run(CommandSender sender)
    {
        sender.sendRichMessage("<light_purple>Please use MiniMessage components for this command, so when players hover over the command, they can see the description of the command.");
    }
}
