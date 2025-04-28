package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.MsgUtility;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

/**
 * Command handler for resuming a paused tournament timer in the EventHorizon plugin.
 * Provides functionality to resume tournament events when they are in a paused state.
 * Requires operator permissions to execute.
 */
public class CommandResume
{
    /**
     * Builds the resume command structure with permission requirements.
     * Creates a command that can only be executed by operators.
     *
     * @param commandName The name of the command to be registered
     * @return LiteralCommandNode containing the configured command structure
     */
    public static LiteralCommandNode<CommandSourceStack> buildCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender().isOp())
                .executes(CommandResume::executeCommandLogic)
                .build();
    }

    /**
     * Executes the resume command logic by attempting to resume the tournament timer.
     * Sends feedback message to the command sender indicating success or failure.
     *
     * @param ctx Command context containing the sender information
     * @return Command success status
     */
    private static int executeCommandLogic(CommandContext<CommandSourceStack> ctx){
        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender

        //Execute command logic here
        MsgUtility.message(sender, EventHorizon.getScheduler().resume() ? "Tournament has resumed" : "<red>ERROR: Cannot resume tournament");
        return Command.SINGLE_SUCCESS;
    }
}
