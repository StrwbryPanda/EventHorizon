package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.commands.CommandRootEventHorizon;
import capstone.team1.eventHorizon.events.utility.fawe.BlockMasks;
import capstone.team1.eventHorizon.commands.CommandsManager;
import capstone.team1.eventHorizon.events.EventInitializer;
import capstone.team1.eventHorizon.events.EventManager;
import com.sk89q.minecraft.util.commands.CommandsManager;
import capstone.team1.eventHorizon.events.utility.EntityAddToWorldListener;
import capstone.team1.eventHorizon.events.utility.PlayerInventoryListener;
import capstone.team1.eventHorizon.events.utility.fawe.BlockMasks;
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

    private static Scheduler scheduler;
    private static EventInitializer eventInitializer;
    private static EventManager eventManager;

    private static EventHorizon plugin;
    private static BlockMasks blockMasks;

    public static Collection<NamespacedKey> entityKeysToDelete = new ArrayList<>();

    public static EventHorizon getPlugin()
    {
        return plugin;
    }

    @Override
    public void onEnable()
    {
        plugin = this;
        blockMasks = new BlockMasks();
        eventInitializer  = new EventInitializer();
        eventManager = new EventManager();
        scheduler = new Scheduler();

        getServer().getPluginManager().registerEvents(new PlayerInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new EntityAddToWorldListener(), this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new PlaceholderEventHorizon().register();
        }
        saveResource("config.yml", /* replace */ false);

        //initializes eventhorizon base command
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(CommandRootEventHorizon.buildCommand());
        });
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

    public static EventInitializer getEventInitializer() {
        return eventInitializer;
    }
    public static BlockMasks getBlockMasks() {
        return blockMasks;
    }
    public static Scheduler getScheduler() {
        return scheduler;
    }
}
