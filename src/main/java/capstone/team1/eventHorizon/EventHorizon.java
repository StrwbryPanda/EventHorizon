package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.commands.CommandsManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnstableApiUsage")

public final class EventHorizon extends JavaPlugin {

    public static EventHorizon plugin;

    @Override
    public void onEnable() {
        plugin = this;

        // Plugin startup logic
        capstone.team1.eventHorizon.ScoreboardManager scoreboardManager = new ScoreboardManager(this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this, scoreboardManager), this);

        saveResource("config.yml", /* replace */ false);

        //initializes eventhorizon base command
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> event.registrar().register("eventhorizon", new CommandsManager(this)));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
