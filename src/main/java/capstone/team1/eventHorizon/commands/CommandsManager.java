package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.ScoreboardManager;
import capstone.team1.eventHorizon.TournamentTimer;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//class that handles the command calls for the plugin
@SuppressWarnings("UnstableApiUsage")
public class CommandsManager implements BasicCommand
{
    private final EventHorizon eventHorizonInstance;
    public static TournamentTimer tournamentTimer;

    public CommandsManager(EventHorizon eventHorizonInstance) {
        this.eventHorizonInstance = eventHorizonInstance;
    }

    //excecutes our base /eventhorizon command and subcommands
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] strings)
    {

        //gets the name of the command sender
        final Component name = commandSourceStack.getExecutor() != null
                ? commandSourceStack.getExecutor().name()
                : commandSourceStack.getSender().name();


        //base /eventhorizon command that executes with no subcommands
        if (strings.length == 0) {
            commandSourceStack.getSender().sendRichMessage("<red>Type /eventhorizon help to see the list of commands");
            return;
        }

        //switch statement to execute subcommands
        String subCommand = strings[0];
        switch (subCommand) {
            case "start":
                CommandStart.run(commandSourceStack.getSender(), eventHorizonInstance);
                break;
            case "stop":
                CommandStop.run(commandSourceStack.getSender(), eventHorizonInstance);
                break;
            case "help":
                CommandHelp.run(commandSourceStack.getSender(), eventHorizonInstance);
                break;
            case "reload":
                Config.reloadConfig();
                commandSourceStack.getSender().sendRichMessage("Config reloaded");
                break;
            case "resume":
                CommandResume.run(commandSourceStack.getSender(), eventHorizonInstance);
                break;
            case "pause":
                CommandPause.run(commandSourceStack.getSender(), eventHorizonInstance);
                break;
            case "trigger":
                // Handle the trigger subcommand with its own arguments
                String[] triggerArgs = new String[strings.length - 1];
                System.arraycopy(strings, 1, triggerArgs, 0, triggerArgs.length);
                CommandTrigger.run(commandSourceStack.getSender(), eventHorizonInstance, triggerArgs);
                break;
            default:
                commandSourceStack.getSender().sendRichMessage("Tournament timer status");
                break;
        }
        commandSourceStack.getSender();
    }

    //suggestions for tab completion of the first tier of subcommands
    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args)
    {
        if (args.length == 0) {
            return List.of("start", "stop", "help", "resume", "pause", "trigger");
        }
        if(args.length == 1)
        {
            return StringUtil.copyPartialMatches(args[0], List.of("start", "stop", "help", "resume", "pause", "trigger"), new ArrayList<>());
        }

        // Handle trigger subcommand tab completions
        if (args.length >= 2 && args[0].equalsIgnoreCase("trigger")) {
            String[] triggerArgs = new String[args.length - 1];
            System.arraycopy(args, 1, triggerArgs, 0, triggerArgs.length);
            return CommandTrigger.getTabCompletions(commandSourceStack.getSender(), triggerArgs);
        }

        return BasicCommand.super.suggest(commandSourceStack, args);
    }

    //allows you to restrict access to the command
    @Override
    public boolean canUse(CommandSender sender)
    {
        return BasicCommand.super.canUse(sender);
    }

    //uses permissions to restrict access to the command
    @Override
    public @Nullable String permission()
    {
        return BasicCommand.super.permission();
    }


}