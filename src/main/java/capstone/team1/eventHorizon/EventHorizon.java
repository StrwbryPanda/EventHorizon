package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.commands.CommandRootEventHorizon;
import capstone.team1.eventHorizon.events.EventInitializer;
import capstone.team1.eventHorizon.events.EventManager;
import capstone.team1.eventHorizon.events.utility.fawe.BlockMasks;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Main plugin class for EventHorizon, a Minecraft tournament management plugin.
 * This class handles plugin initialization, management of core components, and serves as the central access point
 * for various plugin functionalities.
 *
 * <p>The plugin manages tournament scheduling, event handling, and block manipulation through various components.</p>
 *
 * @since 1.0
 */
@SuppressWarnings("UnstableApiUsage")
public final class EventHorizon extends JavaPlugin implements CommandExecutor
{
    /** The tournament scheduler instance */
    private static Scheduler scheduler;

    /** The event initializer instance for setting up tournament events */
    private static EventInitializer eventInitializer;

    /** The event manager instance for handling tournament events */
    private static EventManager eventManager;

    /** Static reference to the plugin instance */
    private static EventHorizon plugin;

    /** Utility for handling block masks in WorldEdit operations */
    private static BlockMasks blockMasks;

    /** Collection of entity keys that need to be cleaned up */
    public static Collection<NamespacedKey> entityKeysToDelete = new ArrayList<>();

    /**
     * Gets the plugin instance.
     * @return The active EventHorizon plugin instance
     */
    public static EventHorizon getPlugin() {
        return plugin;
    }

    /**
     * Called when the plugin is enabled.
     * Initializes core components, sets up PlaceholderAPI integration if available,
     * and registers the main command structure.
     */
    @Override
    public void onEnable() {
        plugin = this;
        blockMasks = new BlockMasks();
        eventInitializer  = new EventInitializer();
        eventManager = new EventManager();
        scheduler = new Scheduler();


        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new PlaceholderEventHorizon().register();
        }
        saveResource("config.yml", /* replace */ false);

        //initializes eventhorizon base command
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(CommandRootEventHorizon.buildCommand());
        });    }

    /**
     * Called when the plugin is disabled.
     * Performs cleanup operations and logs shutdown message.
     */
    @Override
    public void onDisable() {
        getLogger().info("EventHorizon has been disabled.");
    }

    /**
     * Gets the event manager instance.
     * @return The active EventManager instance
     */
    public static EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Gets the event initializer instance.
     * @return The active EventInitializer instance
     */
    public static EventInitializer getEventInitializer() {
        return eventInitializer;
    }

    /**
     * Gets the block masks utility instance.
     * @return The active BlockMasks instance
     */
    public static BlockMasks getBlockMasks() {
        return blockMasks;
    }

    /**
     * Gets the scheduler instance.
     * @return The active Scheduler instance
     */
    public static Scheduler getScheduler() {
        return scheduler;
    }
}