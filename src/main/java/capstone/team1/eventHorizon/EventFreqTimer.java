package capstone.team1.eventHorizon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.util.Random;
import org.bukkit.configuration.file.FileConfiguration;

public class EventFreqTimer {

    private int eventFrequency;
    private double posWeight;
    private double negWeight;
    private double neutralWeight;
    private final Random random = new Random();

    public EventFreqTimer(EventHorizon plugin, int eventFrequency, double posWeight, double negWeight, double neutralWeight){
        FileConfiguration config = plugin.getConfig();

        this.eventFrequency = config.getInt("event.frequency", 600);
        this.posWeight = config.getDouble("event.posWeight", 0.2);
        this.negWeight = config.getDouble("event.negWeight", 0.2);
        this.neutralWeight = config.getDouble("event.neutralWeight", 0.6);
    }

    public void triggerEvent(){
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
