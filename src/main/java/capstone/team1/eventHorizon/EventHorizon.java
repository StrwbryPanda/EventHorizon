package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.commands.CommandsManager;
import capstone.team1.eventHorizon.events.EventFrequencyTimer;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnstableApiUsage")
public final class EventHorizon extends JavaPlugin implements CommandExecutor
{

    public static TournamentTimer tournamentTimer;
    private ScoreboardManager ScoreboardManager;
    private EventFrequencyTimer eventFrequencyTimer;

    public static EventHorizon plugin;

    @Override
    public void onEnable()
    {
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
    public void onDisable()
    {
        // Plugin shutdown logic

        getLogger().info("EventHorizon has been disabled.");
    }


}
