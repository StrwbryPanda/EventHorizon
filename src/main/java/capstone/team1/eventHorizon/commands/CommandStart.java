package capstone.team1.eventHorizon.commands;

import org.bukkit.command.CommandSender;

public class CommandStart
{
    public static void run(CommandSender sender) {
        sender.sendRichMessage("Starting tournament timer");
    }
}
