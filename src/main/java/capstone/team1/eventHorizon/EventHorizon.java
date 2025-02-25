package capstone.team1.eventHorizon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public final class EventHorizon extends JavaPlugin implements CommandExecutor {

    public static TournamentTimer tournamentTimer;
    private ScoreboardManager ScoreboardManager;
    private EventFrequencyTimer eventFrequencyTimer;

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.ScoreboardManager = new ScoreboardManager(this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this, ScoreboardManager), this);

        saveResource("config.yml", /* replace */ false);
        //Commands for timer
        this.getCommand("starttournament").setExecutor(this);
        this.getCommand("stoptournament").setExecutor(this);
        getLogger().info("EventHorizon has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getLogger().info("EventHorizon has been disabled.");
    }

    @Override
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
