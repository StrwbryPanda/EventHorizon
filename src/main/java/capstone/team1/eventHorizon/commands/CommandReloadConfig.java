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

public class CommandReloadConfig
{
    //builds the command
    //allows setting of permissions, subcommands, etc.
    public static LiteralCommandNode<CommandSourceStack> buildCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender().isOp())
                .executes(CommandReloadConfig::executeCommandLogic)
                .build();
    }

    private static int executeCommandLogic(CommandContext<CommandSourceStack> ctx){
        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender

        //Execute command logic here
        Config.reloadConfig();
        EventHorizon.getEventManager().loadWeightsFromConfig();
        EventHorizon.getScheduler().reloadEventFrequency();
        return Command.SINGLE_SUCCESS;
    }
}
