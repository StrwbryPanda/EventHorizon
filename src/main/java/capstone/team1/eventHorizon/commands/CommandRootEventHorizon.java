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

public class CommandRootEventHorizon
{
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
