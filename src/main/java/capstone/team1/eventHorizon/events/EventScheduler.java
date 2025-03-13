package capstone.team1.eventHorizon.events;

import capstone.team1.eventHorizon.Config;
import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.mobSpawn.BaseMobSpawn;
import capstone.team1.eventHorizon.events.mobSpawn.WolfPack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class EventScheduler
{
    private final EventInitializer eventInitializer;
    private double posWeight;
    private double negWeight;
    private double neutralWeight;
    private final Random random = new Random();

    public EventScheduler(EventInitializer eventInitializer)
    {
        this.eventInitializer = eventInitializer;
        this.posWeight = Config.getPosWeight();
        this.negWeight = Config.getNegWeight();
        this.neutralWeight = Config.getNeutralWeight();
        loadWeightsFromConfig();
    }

    public void triggerEvent() {
        Bukkit.getLogger().info("Triggering event...");

        BaseEvent selectedEvent = null;
        List<EventClassification> items = List.of(EventClassification.POSITIVE, EventClassification.NEGATIVE, EventClassification.NEUTRAL);
        List<Double> weights = List.of(posWeight, negWeight, neutralWeight);
        double totalWeight = 0.0;
        for (Double weight : weights) {
            totalWeight += weight;
        }

        Random random = new Random();
        double randomNumber = random.nextDouble() * totalWeight;


        double cumulativeWeight = 0.0;
        for (int i = 0; i < items.size(); i++) {
            cumulativeWeight += weights.get(i);

            if (randomNumber < cumulativeWeight) {
                EventClassification eventClassification = items.get(i);
                List<BaseEvent> selectedEvents = eventInitializer.getEnabledEvents().get(eventClassification);
                selectedEvent = selectedEvents.get(random.nextInt(selectedEvents.size()));
                selectedEvent.execute();
                return;
            }
        }
    }

    private void loadWeightsFromConfig() {
        this.posWeight = Config.getPosWeight();
        this.negWeight = Config.getNegWeight();
        this.neutralWeight = Config.getNeutralWeight();

        Bukkit.getLogger().info("Raw weights - Pos: " + this.posWeight +
                ", Neg: " + this.negWeight +
                ", Neutral: " + this.neutralWeight);
    }
}