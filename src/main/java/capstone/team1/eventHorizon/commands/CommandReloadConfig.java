package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.utility.MsgUtility;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

/**
 * Command handler for reloading the plugin configuration in the EventHorizon plugin.
 * Provides functionality to reload configuration settings, event weights, and event frequency.
 * Requires operator permissions to execute.
 */
public class CommandReloadConfig
{
    /**
     * Builds the reload config command structure with permission requirements.
     * Creates a command that can only be executed by operators.
     *
     * @param commandName The name of the command to be registered
     * @return LiteralCommandNode containing the configured command structure
     */
    public static LiteralCommandNode<CommandSourceStack> buildCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender().isOp())
                .executes(CommandReloadConfig::executeCommandLogic)
                .build();
    }

    /**
     * Executes the reload config command logic by reloading the configuration,
     * event weights, and event frequency settings from the config file.
     *
     * @param ctx Command context containing the sender information
     * @return Command success status
     */
    private static int executeCommandLogic(CommandContext<CommandSourceStack> ctx){
        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender

        //Execute command logic here
        Config.reloadConfig();
        EventHorizon.getEventManager().loadWeightsFromConfig();
        EventHorizon.getScheduler().reloadEventFrequency();
        return Command.SINGLE_SUCCESS;
    }
}
