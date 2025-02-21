package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.commands.CommandsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public final class EventHorizon extends JavaPlugin {

    private ScoreboardManager ScoreboardManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.ScoreboardManager = new ScoreboardManager(this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this, ScoreboardManager), this);

        saveResource("config.yml", /* replace */ false);
        //Commands for timer
        this.getCommand("eventhorizon").setExecutor(new CommandsManager());
        getLogger().info("EventHorizon has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("EventHorizon has been disabled.");
    }





}
