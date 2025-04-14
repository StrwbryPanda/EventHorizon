package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.events.utility.fawe.BlockMasks;
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

/**
 * The main plugin class for EventHorizon, a plugin that introduces randomized events at set intervals for Minecraft servers.
 * This class handles plugin initialization, manages core components, and provides access to essential services.
 */
@SuppressWarnings("UnstableApiUsage")
public final class EventHorizon extends JavaPlugin implements CommandExecutor {

    /** The scheduler responsible for managing tournament timing and event execution. */
    private static Scheduler scheduler;
    /** Handles the initialization of events. */
    private static EventInitializer eventInitializer;
    /** Manages the execution events. */
    private static EventManager eventManager;
    /** The singleton instance of the plugin. */
    private static EventHorizon plugin;
    /** Manages block masks for world editing operations. */
    private static BlockMasks blockMasks;
    /** Collection of entity keys that need to be cleaned up during plugin operation. */
    public static Collection<NamespacedKey> entityKeysToDelete = new ArrayList<>();

    /**
     * Gets the singleton instance of the EventHorizon plugin.
     *
     * @return The plugin instance
     */
    public static EventHorizon getPlugin() {
        return plugin;
    }

    /**
     * Called when the plugin is enabled.
     * Initializes all core components and registers necessary services.
     */
    @Override
    public void onEnable() {
        plugin = this;

        blockMasks = new BlockMasks();
        eventInitializer = new EventInitializer();
        eventManager = new EventManager();
        scheduler = new Scheduler();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderEventHorizon().register();
        }
        saveResource("config.yml", /* replace */ false);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> event.registrar().register("eventhorizon", new CommandsManager()));
    }

    /**
     * Called when the plugin is disabled.
     * Performs cleanup operations and shutdown logic.
     */
    @Override
    public void onDisable() {
        getLogger().info("EventHorizon has been disabled.");
    }

    /**
     * Gets the event manager instance.
     *
     * @return The event manager responsible for handling tournament events
     */
    public static EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Gets the event initializer instance.
     *
     * @return The event initializer responsible for setting up tournament events
     */
    public static EventInitializer getEventInitializer() {
        return eventInitializer;
    }

    /**
     * Gets the block masks instance.
     *
     * @return The block masks utility for world editing operations
     */
    public static BlockMasks getBlockMasks() {
        return blockMasks;
    }

    /**
     * Gets the scheduler instance.
     *
     * @return The scheduler responsible for tournament timing
     */
    public static Scheduler getScheduler() {
        return scheduler;
    }
}