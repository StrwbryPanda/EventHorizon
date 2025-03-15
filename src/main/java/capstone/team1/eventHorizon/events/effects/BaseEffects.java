package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public abstract class BaseEffects extends BaseEvent {
    protected final Plugin plugin;
    protected final NamespacedKey key;
    protected static int globalAmplifier = 1; //amplifier shared by all children
    protected static int globalDuration = 999; //ticks shared by all children

    public BaseEffects(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.plugin;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    // Abstract methods that child classes must implement
    public abstract void start();
    public abstract void end();

    @Override
    public void execute() {
        try {
            start(); // <-- this was missing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setGlobalAmplifier(int amp) {
        globalAmplifier = amp;
    }

    public static int getGlobalAmplifier() {
        return globalAmplifier;
    }
}

//remove start/stop --> execute/terminate
//getter/setter for globalAmplifier and globalDuration
//change 999 to infinite/max_value
//remove potion effect in terminate
//move logic to parent and feed stuff in constructor
//method to add potion effect to one player
//method to call ^ for all players
//same for remove
