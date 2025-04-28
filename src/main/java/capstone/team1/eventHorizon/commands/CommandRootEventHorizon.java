package capstone.team1.eventHorizon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.clip.placeholderapi.commands.impl.local.CommandReload;

import java.util.concurrent.CompletableFuture;

/**
 * Root command handler for the EventHorizon plugin.
 * This class manages the main command structure and provides suggestion handling for subcommands.
 * Creates a command tree with various subcommands like begin, end, help, pause, resume, reloadconfig, and trigger.
 * Uses Brigadier command framework for command registration and handling.
 */
public class CommandRootEventHorizon
{
    /**
     * Builds and returns the main command tree for the EventHorizon plugin.
     * Creates a command structure with subcommands and argument handling using Brigadier framework.
     *
     * @return LiteralCommandNode containing the complete command tree for the plugin
     */
    public static LiteralCommandNode<CommandSourceStack> buildCommand()
    {
        return Commands.literal("eventhorizon")
                .then(CommandBegin.buildCommand("begin"))
                .then(CommandEnd.buildCommand("end"))
                .then(CommandHelp.buildCommand("help"))
                .then(CommandPause.buildCommand("pause"))
                .then(CommandResume.buildCommand("resume"))
                .then(CommandReloadConfig.buildCommand("reloadconfig"))
                .then(CommandTrigger.buildCommand("trigger"))

                .then(Commands.argument("subcommands", StringArgumentType.word())
                        .suggests(CommandRootEventHorizon::getCommandSuggestions)
                )
                .build();
    }

    /**
     * Provides command suggestions for the plugin's subcommands.
     * This method is called by Brigadier to provide tab completion for available subcommands.
     *
     * @param ctx Command context containing the current command state
     * @param builder SuggestionsBuilder used to construct command suggestions
     * @return CompletableFuture containing the built suggestions
     */
    private static CompletableFuture<Suggestions> getCommandSuggestions(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        builder.suggest("begin");
        builder.suggest("end");
        builder.suggest("help");
        builder.suggest("pause");
        builder.suggest("resume");
        builder.suggest("reloadconfig");
        builder.suggest("trigger");
        return builder.buildFuture();
    }
}
