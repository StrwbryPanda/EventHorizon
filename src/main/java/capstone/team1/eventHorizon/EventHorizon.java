package capstone.team1.eventHorizon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventHorizon extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveResource("config.yml", /* replace */ false);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
