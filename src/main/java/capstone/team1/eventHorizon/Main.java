package capstone.team1.eventHorizon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ScoreboardManager ScoreboardManager;

    @Override
    public void onEnable() {
        this.ScoreboardManager = new ScoreboardManager(this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this, ScoreboardManager), this);

    }

    @Override
    public void onDisable() {

    }
}