package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.commands.CommandsManager;
import capstone.team1.eventHorizon.events.EventFrequencyTimer;
import capstone.team1.eventHorizon.events.EventInitializer;
import capstone.team1.eventHorizon.events.EventScheduler;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("UnstableApiUsage")
public final class EventHorizon extends JavaPlugin implements CommandExecutor
{

    private TournamentTimer tournamentTimer;
    private ScoreboardManager scoreboardManager;
    public static EventFrequencyTimer eventFrequencyTimer;
    private EventInitializer eventInitializer;
    private EventScheduler eventScheduler;


    public static EventHorizon plugin;
    public boolean isScoreboardOn;
    public static Collection<NamespacedKey> entityKeysToDelete = new ArrayList<>();

    @Override
    public void onEnable()
    {
        plugin = this;

        // Plugin startup logic
        setCommandScoreboard(plugin);
        this.scoreboardManager = new ScoreboardManager();
        this.eventInitializer  = new EventInitializer();
        this.eventScheduler  = new EventScheduler(eventInitializer);
        eventFrequencyTimer = new EventFrequencyTimer(eventScheduler);



        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this, scoreboardManager), this);

        saveResource("config.yml", /* replace */ false);

        //initializes eventhorizon base command
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> event.registrar().register("eventhorizon", new CommandsManager(this)));

    }

    private void setCommandScoreboard(EventHorizon plugin) {
        this.isScoreboardOn = plugin.getConfig().getBoolean("scoreboard.setting", Config.getScoreboardSetting()); //use config value
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        getLogger().info("EventHorizon has been disabled.");
    }


}
