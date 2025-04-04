package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.Config;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//class that handles the command calls for the plugin
@SuppressWarnings("UnstableApiUsage")
public class CommandsManager implements BasicCommand
{

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
            case "begin":
                CommandBegin.run(commandSourceStack.getSender());
                break;
            case "end":
                CommandEnd.run(commandSourceStack.getSender());
                break;
            case "help":
                CommandHelp.run(commandSourceStack.getSender());
                break;
            case "reload":
                Config.reloadConfig();
                commandSourceStack.getSender().sendRichMessage("Config reloaded");
                break;
            case "resume":
                CommandResume.run(commandSourceStack.getSender());
                break;
            case "pause":
                CommandPause.run(commandSourceStack.getSender());
                break;
            case "trigger":
                // Handle the trigger subcommand with its own arguments
                String[] triggerArgs = new String[strings.length - 1];
                System.arraycopy(strings, 1, triggerArgs, 0, triggerArgs.length);
                CommandTrigger.run(commandSourceStack.getSender(), triggerArgs);
                break;
//            case "yes": //dont inlcude these in CommandHelp. this is just for confirming cancellation in CommandEnd
//                CommandEnd.yes(commandSourceStack.getSender());
//                break;
//            case "no": //dont inlcude these in CommandHelp. this is just for confirming cancellation in CommandEnd
//                CommandEnd.no(commandSourceStack.getSender());
//                break;

            default:
                commandSourceStack.getSender().sendRichMessage("<red>Invalid subcommand. Type /eventhorizon help to see the list of commands");
                break;
        }
        commandSourceStack.getSender();
    }

    //suggestions for tab completion of the first tier of subcommands
    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args)
    {
        if (args.length == 0) {
            return List.of("begin", "end", "help", "resume", "pause", "trigger");
        }
        if(args.length == 1)
        {
            return StringUtil.copyPartialMatches(args[0], List.of("begin", "end", "help", "resume", "pause", "trigger"), new ArrayList<>());
        }

        // Handle trigger subcommand tab completions
        if (args.length == 2 && args[0].equalsIgnoreCase("trigger")) {
            String[] triggerArgs = new String[args.length - 1];
            System.arraycopy(args, 1, triggerArgs, 0, triggerArgs.length);
            return CommandTrigger.suggest(commandSourceStack.getSender(), triggerArgs);
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