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

//command that starts the tournament timer
public class CommandBegin
{
    //builds the command
    //allows setting of permissions, subcommands, etc.
    public static LiteralCommandNode<CommandSourceStack> buildCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender().isOp())
                .executes(CommandBegin::executeCommandLogic)
                .build();
    }

    private static int executeCommandLogic(CommandContext<CommandSourceStack> ctx){
        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender
        //Execute command logic here
        MsgUtility.message(sender, EventHorizon.getScheduler().start(Config.getTournamentTimer()) ? "The tournament has started" : "<red>ERROR: Tournament already started");
        return Command.SINGLE_SUCCESS;
    }
}
