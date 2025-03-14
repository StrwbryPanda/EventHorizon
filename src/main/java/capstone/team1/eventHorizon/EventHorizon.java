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

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("UnstableApiUsage")
public final class EventHorizon extends JavaPlugin implements CommandExecutor
{

    public static GameTimer gameTimer;
    private ScoreboardManager scoreboardManager;
    public static Scheduler scheduler;
    private EventInitializer eventInitializer;
    private EventManager eventManager;


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
        this.scoreboardManager = new ScoreboardManager();
        this.eventInitializer  = new EventInitializer();
        this.eventManager = new EventManager(eventInitializer);
        scheduler = new Scheduler(eventManager);

        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this, scoreboardManager), this);

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
}
