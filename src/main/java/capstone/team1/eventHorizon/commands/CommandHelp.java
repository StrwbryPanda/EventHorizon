package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.utility.MsgUtility;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandHelp {

    private static final Map<String, String> commands = new LinkedHashMap<>(); // Store command names and descriptions

    //builds the command
    //allows setting of permissions, subcommands, etc.
    public static LiteralCommandNode<CommandSourceStack> buildCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender().isOp())
                .executes(CommandHelp::executeCommandLogic)
                .build();
    }

    private static int executeCommandLogic(CommandContext<CommandSourceStack> ctx){
        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender

        //Execute command logic here
        loadCommands();
        sender.sendMessage(Component.text("§aEvent Horizon Commands:"));
        for (String key : commands.keySet() ) {
            sender.sendMessage((MiniMessage.miniMessage().deserialize("<hover:show_text:'<gray>" + commands.get(key) + "'><gray>/eventhorizon " + key)));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void loadCommands() {
        commands.put("begin", "Starts the Event Horizon tournament timer.");
        commands.put("end", "Cancels the Event Horizon tournament timer.");
        commands.put("help", "Displays available Event Horizon commands.");
        commands.put("pause", "Pauses the Event Horizon tournament timer.");
        commands.put("reloadconfig", "Reloads the Event Horizon configuration file.");
        commands.put("resume", "Resumes the Event Horizon tournament timer.");
        commands.put("trigger", "Allows user to manually trigger events by name.");
    }
}


