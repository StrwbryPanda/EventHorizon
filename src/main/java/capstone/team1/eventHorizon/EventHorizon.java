package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.Utility.Config;
import capstone.team1.eventHorizon.commands.CommandsManager;
import capstone.team1.eventHorizon.events.EventInitializer;
import capstone.team1.eventHorizon.events.EventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("UnstableApiUsage")
public final class EventHorizon extends JavaPlugin implements CommandExecutor
{

    public static Scheduler scheduler;
    private EventInitializer eventInitializer;
    private static EventManager eventManager;

    public static EventHorizon plugin;
    public boolean isScoreboardOn;
    public static Collection<NamespacedKey> entityKeysToDelete = new ArrayList<>();

    public static EventHorizon getPlugin()
    {
        return plugin;
    }

    @Override
    public void onEnable()
    {
        plugin = this;

        // Plugin startup logic
        setCommandScoreboard(plugin);

        this.eventInitializer  = new EventInitializer();
        eventManager = new EventManager(eventInitializer);
        scheduler = new Scheduler(eventManager);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new PlaceholderEventHorizon().register();
        }
        saveResource("config.yml", /* replace */ false);

        //initializes eventhorizon base command
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> event.registrar().register("eventhorizon", new CommandsManager()));
    }

    private void setCommandScoreboard(EventHorizon plugin) {
        this.isScoreboardOn = Config.getScoreboardSetting(); //use config value
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        getLogger().info("EventHorizon has been disabled.");
    }

    public static EventManager getEventManager() {
        return eventManager;
    }
}
