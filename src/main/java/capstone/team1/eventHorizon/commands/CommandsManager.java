package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.TournamentTimer;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
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

        final Component name = commandSourceStack.getExecutor() != null
                ? commandSourceStack.getExecutor().name()
                : commandSourceStack.getSender().name();


        if (strings.length == 0) {
            commandSourceStack.getSender().sendRichMessage("<red>Type /eventhorizon help to see the list of commands");
            return;
        }
        String subCommand = strings[0];
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
        commandSourceStack.getSender();
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
