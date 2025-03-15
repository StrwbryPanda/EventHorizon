package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AttributesBase extends BaseEvent{
    protected final JavaPlugin plugin;
    protected final NamespacedKey key;

    public AttributesBase(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.plugin;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public abstract void applyEffect(Player player);

    @Override
    public void execute() {
        // Apply effect to all players currently online
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            applyEffect(player);
        }
    }
}
