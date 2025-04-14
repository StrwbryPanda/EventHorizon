package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.utility.MsgUtility;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CommandTrigger
{
    //builds the command
    //allows setting of permissions, subcommands, etc.
    public static LiteralCommandNode<CommandSourceStack> buildCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender().isOp())
                .then(Commands.argument("eventName", StringArgumentType.word())
                        .suggests(CommandTrigger::getCommandSuggestions)
                        .executes(CommandTrigger::executeCommandLogic)
                )
                .build();
    }

    private static int executeCommandLogic(CommandContext<CommandSourceStack> ctx){
        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender

        //Execute command logic here
        final String eventName = StringArgumentType.getString(ctx, "eventName");
        MsgUtility.warning("Triggering event..." + eventName);
        EventHorizon.getEventManager().triggerEventByName(eventName);
        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> getCommandSuggestions(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        Collection<String> eventNames = EventHorizon.getEventInitializer().getRegisteredEvents().keySet();
        for (String eventName : eventNames) {
                builder.suggest(eventName);
        }
        return builder.buildFuture();
    }
}