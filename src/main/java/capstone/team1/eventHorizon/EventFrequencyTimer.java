package capstone.team1.eventHorizon;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;
import java.util.Random;
import org.bukkit.configuration.file.FileConfiguration;

public class EventFrequencyTimer extends BukkitRunnable {

    private int eventFrequency;
    private double posWeight;
    private double negWeight;
    private double neutralWeight;
    private final Random random = new Random();
    private final EventHorizon plugin;

    public EventFrequencyTimer(EventHorizon plugin){

        FileConfiguration config = plugin.getConfig();
        this.plugin = plugin;
        this.eventFrequency = config.getInt("event.frequency", 10);
        this.posWeight = config.getDouble("event.posWeight", 0.2);
        this.negWeight = config.getDouble("event.negWeight", 0.2);
        this.neutralWeight = config.getDouble("event.neutralWeight", 0.6);
    }

    @Override
    public void run()
    {
        Bukkit.getLogger().info("EventFrequencyTimer running...");
        triggerEvent();
    }

    public void startTimer()
    {
        Bukkit.getLogger().info("Starting EventFrequencyTimer with frequency: " + eventFrequency + " seconds.");
        this.runTaskTimer(plugin, 0L, eventFrequency * 20L); //20 ticks per second
    }

    public void triggerEvent(){
        Bukkit.getLogger().info("Triggering event...");
        double randomNumber = random.nextDouble();
        if(randomNumber < posWeight){
            Bukkit.broadcastMessage(ChatColor.GREEN + "Positive event");
        }
        else if(randomNumber < posWeight + negWeight){
            Bukkit.broadcastMessage(ChatColor.RED + "Negative event");
        }
        else{
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Neutral event");
        }
    }
}
