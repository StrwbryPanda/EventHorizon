package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

public abstract class BaseAttribute extends BaseEvent{
    protected final Plugin plugin;
    protected final Random random = new Random();
    protected final NamespacedKey key;

    // Default configuration values
    private static final double DEFAULT_AMOUNT = 1;
    private static final AttributeModifier.Operation DEFAULT_OPERATION = AttributeModifier.Operation.ADD_NUMBER;

    // Attributes and modifiers map
    protected Map<Attribute, List<AttributeModifier>> attributeModifiers = new HashMap<>();

    // Constructors
    public BaseAttribute(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseAttribute(Attribute attribute, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);

        addAttributeModifier(attribute, DEFAULT_AMOUNT, DEFAULT_OPERATION);
    }

    public BaseAttribute(Attribute attribute, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);

        addAttributeModifier(attribute, DEFAULT_AMOUNT, DEFAULT_OPERATION);
    }

    // Executes the event
    @Override
    public void execute() {
        MsgUtility.log("<green>Executing attribute event: " + this.eventName);
        applyAttributeModifiersToAllPlayers();
    }

    // Terminates the event
    @Override
    public void terminate() {
        MsgUtility.log("<red>Terminating attribute event: " + this.eventName);
        removeAttributeModifiersFromAllPlayers();
    }

    // Applies attribute modifiers to all players
    public void applyAttributeModifiersToAllPlayers() {
        int successCount = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            try {
                applyAttributeModifiersToPlayer(player);
                successCount++;
            } catch (Exception e) {
                MsgUtility.warning("Failed to apply attributes to player " + player.getName() + ": " + e.getMessage());
            }
        }
        MsgUtility.log("Applied attribute modifiers to " + successCount + "/" + players.size() + " players for event: " + this.eventName);
    }

    // Applies attribute modifiers to a player
    public void applyAttributeModifiersToPlayer(Player player) {
        for (Map.Entry<Attribute, List<AttributeModifier>> entry : attributeModifiers.entrySet()) {
            Attribute attribute = entry.getKey();
            List<AttributeModifier> modifiers = entry.getValue();
            AttributeInstance attributeInstance = player.getAttribute(attribute);

            if (attributeInstance != null) {
                for (AttributeModifier modifier : modifiers) {
                    // Remove existing modifier with the same key to avoid duplicates
                    attributeInstance.getModifiers().stream()
                            .filter(m -> modifier.getKey().equals(m.getKey()))
                            .forEach(attributeInstance::removeModifier);

                    attributeInstance.addModifier(modifier);
                }
            }
        }
        markAttributePlayer(player);
    }

    // Removes attribute modifiers from all players
    public void removeAttributeModifiersFromAllPlayers() {
        int successCount = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            try {
                removeAttributeModifiersFromPlayer(player);
                successCount++;
            } catch (Exception e) {
                MsgUtility.warning("Failed to remove attributes from player " + player.getName() + ": " + e.getMessage());
            }
        }
        MsgUtility.log("Removed attribute modifiers from " + successCount + "/" + players.size() + " players for event: " + this.eventName);
    }

    // Removes attribute modifiers from a player
    public void removeAttributeModifiersFromPlayer(Player player) {
        for (Map.Entry<Attribute, List<AttributeModifier>> entry : attributeModifiers.entrySet()) {
            Attribute attribute = entry.getKey();
            List<AttributeModifier> modifiers = entry.getValue();
            AttributeInstance attributeInstance = player.getAttribute(attribute);

            if (attributeInstance != null) {
                for (AttributeModifier modifier : modifiers) {
                    attributeInstance.getModifiers().stream()
                            .filter(m -> modifier.getKey().equals(m.getKey()))
                            .forEach(attributeInstance::removeModifier);
                }
            }
        }
        unmarkAttributePlayer(player);
    }

    // Methods to add and remove attribute modifiers
    public BaseAttribute addAttributeModifier(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        NamespacedKey modifierKey = new NamespacedKey(plugin, this.eventName + "_" + attribute.toString());
        AttributeModifier modifier = new AttributeModifier(modifierKey, amount, operation);

        if (!attributeModifiers.containsKey(attribute)) {
            attributeModifiers.put(attribute, new ArrayList<>());
        }
        attributeModifiers.get(attribute).add(modifier);

        return this;
    }

    public BaseAttribute addAttributeModifier(Attribute attribute) {
        return addAttributeModifier(attribute, DEFAULT_AMOUNT, DEFAULT_OPERATION);
    }

    public BaseAttribute addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        if (!attributeModifiers.containsKey(attribute)) {
            attributeModifiers.put(attribute, new ArrayList<>());
        }
        attributeModifiers.get(attribute).add(modifier);

        return this;
    }

    public BaseAttribute removeAttributeModifiers(Attribute attribute) {
        attributeModifiers.remove(attribute);
        return this;
    }

    public List<AttributeModifier> getAttributeModifiers(Attribute attribute) {
        return attributeModifiers.getOrDefault(attribute, new ArrayList<>());
    }

    public boolean hasAttributeModifiers(Attribute attribute) {
        return attributeModifiers.containsKey(attribute) && !attributeModifiers.get(attribute).isEmpty();
    }

    public void markAttributePlayer(Player player) {
        player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    public boolean isAttributePlayerMarked(Player player) {
        return player.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    public void unmarkAttributePlayer(Player player) {
        player.getPersistentDataContainer().remove(key);
    }

    public void removeAttributesFromAllMarkedPlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            if (isAttributePlayerMarked(player)) {
                removeAttributeModifiersFromPlayer(player);
            }
        }
    }

    public Map<Attribute, List<AttributeModifier>> getAllAttributeModifiers() {
        return new HashMap<>(attributeModifiers);
    }
}
