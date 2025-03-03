package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class EventFrequencyTimer extends BukkitRunnable {
    private final int eventFrequency;
    private final EventScheduler eventScheduler;
    private final EventHorizon plugin;

    public EventFrequencyTimer(EventHorizon plugin) {
        this.plugin = plugin;
        this.eventFrequency = plugin.getConfig().getInt("event.frequency", Config.getEventFrequency());
        this.eventScheduler = new EventScheduler(plugin);
    }

    @Override
    public void run() {
        Bukkit.getLogger().info("EventFrequencyTimer running...");
        eventScheduler.triggerEvent();
    }

    public void startTimer() {
        Bukkit.getLogger().info("Starting EventFrequencyTimer with frequency: " + eventFrequency + " seconds.");
        this.runTaskTimer(plugin, 0L, eventFrequency * 20L);
    }
}