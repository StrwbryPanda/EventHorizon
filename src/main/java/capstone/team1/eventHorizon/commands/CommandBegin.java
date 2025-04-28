package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.MsgUtility;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

/**
 * Command handler for starting the tournament timer in the EventHorizon plugin.
 * Provides functionality to initiate tournament events with configured duration.
 * Requires operator permissions to execute.
 */
public class CommandBegin
{
    /**
     * Builds the begin command structure with permission requirements.
     * Creates a command that can only be executed by operators.
     *
     * @param commandName The name of the command to be registered
     * @return LiteralCommandNode containing the configured command structure
     */
    public static LiteralCommandNode<CommandSourceStack> buildCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender().isOp())
                .executes(CommandBegin::executeCommandLogic)
                .build();
    }

    /**
     * Executes the begin command logic by starting the tournament timer.
     * Sends feedback message to the command sender indicating success or failure.
     *
     * @param ctx Command context containing the sender information
     * @return Command success status
     */
    private static int executeCommandLogic(CommandContext<CommandSourceStack> ctx){
        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender
        //Execute command logic here
        MsgUtility.message(sender, EventHorizon.getScheduler().start(Config.getTournamentTimer()) ? "The tournament has started" : "<red>ERROR: Tournament already started");
        return Command.SINGLE_SUCCESS;
    }
}
