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

/**
 * Command handler for manually triggering events in the EventHorizon plugin.
 * Provides functionality to trigger specific events by name with tab completion support.
 * Requires operator permissions to execute.
 */
public class CommandTrigger
{
    /**
     * Builds the trigger command structure with argument handling for event names.
     * Sets up permission requirements and tab completion for available events.
     *
     * @param commandName The name of the command to be registered
     * @return LiteralCommandNode containing the configured command structure
     */
    public static LiteralCommandNode<CommandSourceStack> buildCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender().isOp())
                .then(Commands.argument("eventName", StringArgumentType.word())
                        .suggests(CommandTrigger::getCommandSuggestions)
                        .executes(CommandTrigger::executeCommandLogic)
                )
                .build();
    }

    /**
     * Executes the trigger command logic by retrieving the specified event name
     * and triggering the corresponding event through the EventManager.
     *
     * @param ctx Command context containing the event name argument
     * @return Command success status
     */
    private static int executeCommandLogic(CommandContext<CommandSourceStack> ctx){
        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender

        //Execute command logic here
        final String eventName = StringArgumentType.getString(ctx, "eventName");
        MsgUtility.warning("Triggering event..." + eventName);
        EventHorizon.getEventManager().triggerEventByName(eventName);
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Provides tab completion suggestions for available event names.
     * Retrieves registered event names from the EventInitializer.
     *
     * @param ctx Command context for the suggestion request
     * @param builder SuggestionsBuilder for adding event name suggestions
     * @return CompletableFuture containing the event name suggestions
     */
    private static CompletableFuture<Suggestions> getCommandSuggestions(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        Collection<String> eventNames = EventHorizon.getEventInitializer().getRegisteredEvents().keySet();
        for (String eventName : eventNames) {
                builder.suggest(eventName);
        }
        return builder.buildFuture();
    }
}