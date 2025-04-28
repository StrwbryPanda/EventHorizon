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

/**
 * Command handler for displaying help information in the EventHorizon plugin.
 * Provides a list of available commands and their descriptions with hover tooltips.
 * Requires operator permissions to execute.
 */
public class CommandHelp {

    /**
     * Stores command names and their corresponding descriptions for help display.
     * Uses LinkedHashMap to maintain insertion order when displaying commands.
     */
    private static final Map<String, String> commands = new LinkedHashMap<>(); // Store command names and descriptions

    /**
     * Builds the help command structure with permission requirements.
     * Creates a command that can only be executed by operators.
     *
     * @param commandName The name of the command to be registered
     * @return LiteralCommandNode containing the configured command structure
     */
    public static LiteralCommandNode<CommandSourceStack> buildCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender().isOp())
                .executes(CommandHelp::executeCommandLogic)
                .build();
    }

    /**
     * Executes the help command logic by displaying a list of available commands
     * with descriptions shown in hover tooltips.
     *
     * @param ctx Command context containing the sender information
     * @return Command success status
     */
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

    /**
     * Loads the command descriptions into a map for display.
     * Initializes descriptions for all available EventHorizon commands.
     */
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


