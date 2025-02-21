package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.TournamentTimer;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

@SuppressWarnings("UnstableApiUsage")
public class CommandsManager implements BasicCommand
{
    public static TournamentTimer tournamentTimer;

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] strings)
    {
        if (!command.getName().equalsIgnoreCase("eventhorizon")) {
            return false;
        }
        if (strings.length == 1) {
            commandSourceStack.getSender().sendMessage("EventHorizon base command");
            return true;
        }
        String subCommand = args[0];
        switch (subCommand) {
            case "start":
                CommandStart.run(commandSourceStack.getSender());
                break;
            case "stop":
                commandSourceStack.getSender().sendRichMessage("Stopping tournament timer");
                break;
            default:
                commandSourceStack.getSender().sendRichMessage("Tournament timer status");
                break;

        }
        commandSourceStack.getSender()
        return true;
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args)
    {
        return BasicCommand.super.suggest(commandSourceStack, args);
    }

    @Override
    public boolean canUse(CommandSender sender)
    {
        return BasicCommand.super.canUse(sender);
    }

    @Override
    public @Nullable String permission()
    {
        return BasicCommand.super.permission();
    }
}
