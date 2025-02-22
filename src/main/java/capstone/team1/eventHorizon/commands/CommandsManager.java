package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.TournamentTimer;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

@SuppressWarnings("UnstableApiUsage")
public class CommandsManager implements BasicCommand
{
    public static TournamentTimer tournamentTimer;

    //excecutes our base /eventhorizon command and subcommands
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] strings)
    {

        final Component name = commandSourceStack.getExecutor() != null
                ? commandSourceStack.getExecutor().name()
                : commandSourceStack.getSender().name();


        //base /eventhorizon command that executes with no subcommands
        if (strings.length == 0) {
            commandSourceStack.getSender().sendRichMessage("<red>Type /eventhorizon help to see the list of commands");
            return;
        }

        //switch statement to execute subcommands
        String subCommand = strings[0];
        switch (subCommand) {
            case "start":
                CommandStart.run(commandSourceStack.getSender());
                break;
            case "stop":
                commandSourceStack.getSender().sendRichMessage("Stopping tournament timer");
                break;
            default:
                commandSourceStack.getSender().sendRichMessage("Tournament timer status");
                break;

        }
        commandSourceStack.getSender();
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args)
    {
        return BasicCommand.super.suggest(commandSourceStack, args);
    }

    @Override
    public boolean canUse(CommandSender sender)
    {
        return BasicCommand.super.canUse(sender);
    }

    @Override
    public @Nullable String permission()
    {
        return BasicCommand.super.permission();
    }



    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("starttournament")) {
            // if command statement to start timer is used then start tournament timer
            if (tournamentTimer == null || tournamentTimer.isCancelled()) {
                tournamentTimer = new TournamentTimer(this);
                tournamentTimer.startTimer();
                sender.sendMessage("Tournament timer started!");
                //Timer already running
            } else {
                sender.sendMessage("Tournament is already running!");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("stoptournament")) {
            // if command statement to stop timer is used then stop tournament timer
            if (tournamentTimer != null && !tournamentTimer.isCancelled()) {
                tournamentTimer.cancel();
                sender.sendMessage("Tournament timer stopped!");
                //Timer is already stop
            } else {
                sender.sendMessage("No tournament is running.");
            }
            return true;
        }
        return false;
    }

}
