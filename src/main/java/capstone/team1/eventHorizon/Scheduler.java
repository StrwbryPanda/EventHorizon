package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Scheduler extends BukkitRunnable {
    private final int eventFrequency;
    private final EventManager eventManager;
    private final EventHorizon plugin;

    public Scheduler(EventManager eventManager) {
        this.plugin = EventHorizon.plugin;
        this.eventFrequency = plugin.getConfig().getInt("event.frequency", Config.getEventFrequency());
        this.eventManager = eventManager;  // Use the passed eventScheduler instead of creating new one
    }

    @Override
    public void run() {
        Bukkit.getLogger().info("EventFrequencyTimer running...");
        eventManager.triggerEvent();
    }

    public void startTimer() {
        Bukkit.getLogger().info("Starting EventFrequencyTimer with frequency: " + eventFrequency + " seconds.");
        this.runTaskTimer(plugin, 0L, eventFrequency * 20L);
    }

    //min = 5mins
    //max = 10 mins
    //random (5,10)
    //timertracker += random
}