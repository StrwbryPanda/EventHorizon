package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.mobSpawn.BaseMobSpawn;
import capstone.team1.eventHorizon.events.mobSpawn.WolfPack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class EventScheduler
{
    private final Random random = new Random();
    private final EventHorizon plugin;
    private final List<BaseEvent> events = new ArrayList<>();

    private double posWeight;
    private double negWeight;
    private double neutralWeight;




    public EventScheduler(EventHorizon plugin)
    {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        this.posWeight = config.getDouble("event.posWeight", 1.0);
        this.negWeight = config.getDouble("event.negWeight", 0.0);
        this.neutralWeight = config.getDouble("event.neutralWeight", 0.0);    }

    public void triggerEvent()
    {
        Bukkit.getLogger().info("Triggering event...");
        double totalWeight = posWeight + negWeight + neutralWeight;
        double normalizedPosWeight = posWeight / totalWeight;
        double normalizedNegWeight = negWeight / totalWeight;
        double randomNumber = random.nextDouble();
        if(randomNumber < normalizedPosWeight){
            Bukkit.broadcastMessage(ChatColor.GREEN + "Positive event");
        }
        else if(randomNumber < normalizedPosWeight + normalizedNegWeight){
            Bukkit.broadcastMessage(ChatColor.RED + "Negative event");
        }
        else{
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Neutral event");
        }
    }
}