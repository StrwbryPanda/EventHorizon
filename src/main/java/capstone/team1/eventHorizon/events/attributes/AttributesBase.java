package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public abstract class AttributesBase extends BaseEvent{
    protected final Plugin plugin;
    protected final Random random = new Random();
    protected final NamespacedKey key;

    // Default configuration values
    private static final double DEFAULT_AMOUNT = 1;
    private static final AttributeModifier.Operation DEFAULT_OPERATION = AttributeModifier.Operation.ADD_NUMBER;

    // Attribute properties
    public Attribute attributeType = Attribute.MAX_HEALTH;
    public double amount = DEFAULT_AMOUNT;
    public AttributeModifier.Operation operation = DEFAULT_OPERATION;

    // Constructors
    public AttributesBase(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.plugin;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public abstract void applyAttribute(Player player);

    @Override
    public void execute() {
        // Apply effect to all players currently online
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            applyAttribute(player);
        }
    }

    @Override
    public void terminate() {
        removeAttributeFromAllPlayers();
    }

    public void applyAttributeToAllPlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            applyAttributeToPlayer(player);
        }
    }

    public void applyAttributeToPlayer(Player player) {

    }

    public void removeAttributeFromAllPlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            removeAttributeFromPlayer(player);
        }
    }

    public void removeAttributeFromPlayer(Player player) {

    }

    // Setters
    public AttributesBase setAttributeType(Attribute attributeType) {
        this.attributeType = attributeType;
        return this;
    }

    public AttributesBase setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public AttributesBase setOperation(AttributeModifier.Operation operation) {
        this.operation = operation;
        return this;
    }

}
