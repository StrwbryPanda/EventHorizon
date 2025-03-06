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


    public EventScheduler(EventHorizon plugin)
    {
        this.plugin = plugin;
    }

    public void triggerEvent()
    {
    }
}