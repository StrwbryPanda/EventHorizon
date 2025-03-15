package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.utility.Config;
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

    public static Scheduler scheduler;
    private EventInitializer eventInitializer;
    private static EventManager eventManager;

    private static EventHorizon plugin;

    public static Collection<NamespacedKey> entityKeysToDelete = new ArrayList<>();

    public static EventHorizon getPlugin()
    {
        return plugin;
    }

    @Override
    public void onEnable()
    {
        plugin = this;

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
