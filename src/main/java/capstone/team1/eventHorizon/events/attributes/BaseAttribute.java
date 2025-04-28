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

import java.util.*;

/**
 * Abstract base class for managing player attribute modifications in the EventHorizon plugin.
 * This class provides functionality to apply and remove attribute modifiers to players,
 * track modified players, and manage attribute modification events.
 */
public abstract class BaseAttribute extends BaseEvent {
    /** Reference to the main plugin instance */
    EventHorizon plugin = EventHorizon.getPlugin();
    /** Random number generator for attribute-related calculations */
    protected final Random random = new Random();
    /** Unique identifier for this attribute event */
    protected final NamespacedKey key;

    // Default configuration values
    /** Default value for attribute modifications */
    private static final double DEFAULT_AMOUNT = 1;
    /** Default operation type for attribute modifications */
    private static final AttributeModifier.Operation DEFAULT_OPERATION = AttributeModifier.Operation.ADD_NUMBER;

    // Attributes and modifiers map
    /**
     * Maps Bukkit Attributes to their corresponding AttributeModifiers.
     * Each Attribute can have multiple modifiers applied to it.
     */
    protected Map<Attribute, List<AttributeModifier>> attributeModifiers = new HashMap<>();

    // Constructors
    /**
     * Constructs a new BaseAttribute with the specified classification and event name.
     * @param classification The event classification (POSITIVE, NEUTRAL, or NEGATIVE)
     * @param eventName The unique name for this attribute event
     */
    public BaseAttribute(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    /**
     * Constructs a new BaseAttribute with default attribute modifier settings.
     * @param attribute The Bukkit attribute to modify
     * @param eventName The unique name for this attribute event
     */
    public BaseAttribute(Attribute attribute, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);

        addAttributeModifier(attribute, DEFAULT_AMOUNT, DEFAULT_OPERATION);
    }

    /**
     * Constructs a new BaseAttribute with specified classification and default modifier settings.
     * @param attribute The Bukkit attribute to modify
     * @param classification The event classification
     * @param eventName The unique name for this attribute event
     */
    public BaseAttribute(Attribute attribute, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);

        addAttributeModifier(attribute, DEFAULT_AMOUNT, DEFAULT_OPERATION);
    }

    /**
     * Executes the attribute event by applying modifiers to all online players.
     */
    @Override
    public void execute() {
        MsgUtility.log("<green>Executing attribute event: " + this.eventName);
        applyAttributeModifiersToAllPlayers();
    }

    /**
     * Terminates the attribute event by removing modifiers from all affected players.
     */
    @Override
    public void terminate() {
        MsgUtility.log("<red>Terminating attribute event: " + this.eventName);
        removeAttributeModifiersFromAllPlayers();
    }

    /**
     * Applies all registered attribute modifiers to every online player.
     */
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

    /**
     * Applies all registered attribute modifiers to a specific player.
     * @param player The player to receive the attribute modifications
     */
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

    /**
     * Removes all attribute modifiers from every online player.
     */
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

    /**
     * Removes all attribute modifiers from a specific player.
     * @param player The player to remove attribute modifications from
     */
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

    /**
     * Adds a new attribute modifier with specified parameters.
     * @param attribute The Bukkit attribute to modify
     * @param amount The modification amount
     * @param operation The type of modification operation
     * @return This BaseAttribute instance for method chaining
     */
    public BaseAttribute addAttributeModifier(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        NamespacedKey modifierKey = new NamespacedKey(plugin, this.eventName + "_" + attribute.toString());
        AttributeModifier modifier = new AttributeModifier(modifierKey, amount, operation);

        if (!attributeModifiers.containsKey(attribute)) {
            attributeModifiers.put(attribute, new ArrayList<>());
        }
        attributeModifiers.get(attribute).add(modifier);

        return this;
    }

    /**
     * Adds a new attribute modifier with default settings.
     * @param attribute The Bukkit attribute to modify
     * @return This BaseAttribute instance for method chaining
     */
    public BaseAttribute addAttributeModifier(Attribute attribute) {
        return addAttributeModifier(attribute, DEFAULT_AMOUNT, DEFAULT_OPERATION);
    }

    /**
     * Adds a pre-configured attribute modifier.
     * @param attribute The Bukkit attribute to modify
     * @param modifier The pre-configured AttributeModifier
     * @return This BaseAttribute instance for method chaining
     */
    public BaseAttribute addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        if (!attributeModifiers.containsKey(attribute)) {
            attributeModifiers.put(attribute, new ArrayList<>());
        }
        attributeModifiers.get(attribute).add(modifier);

        return this;
    }

    /**
     * Removes all modifiers for a specific attribute.
     * @param attribute The Bukkit attribute to remove modifiers from
     * @return This BaseAttribute instance for method chaining
     */
    public BaseAttribute removeAttributeModifiers(Attribute attribute) {
        attributeModifiers.remove(attribute);
        return this;
    }

    /**
     * Retrieves all modifiers for a specific attribute.
     * @param attribute The Bukkit attribute to get modifiers for
     * @return List of AttributeModifiers for the specified attribute
     */
    public List<AttributeModifier> getAttributeModifiers(Attribute attribute) {
        return attributeModifiers.getOrDefault(attribute, new ArrayList<>());
    }

    /**
     * Checks if an attribute has any modifiers registered.
     * @param attribute The Bukkit attribute to check
     * @return true if the attribute has modifiers, false otherwise
     */
    public boolean hasAttributeModifiers(Attribute attribute) {
        return attributeModifiers.containsKey(attribute) && !attributeModifiers.get(attribute).isEmpty();
    }

    /**
     * Marks a player as affected by this attribute event.
     * @param player The player to mark
     */
    public void markAttributePlayer(Player player) {
        player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    /**
     * Checks if a player is marked as affected by this attribute event.
     * @param player The player to check
     * @return true if the player is marked, false otherwise
     */
    public boolean isAttributePlayerMarked(Player player) {
        return player.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    /**
     * Removes the attribute event mark from a player.
     * @param player The player to unmark
     */
    public void unmarkAttributePlayer(Player player) {
        player.getPersistentDataContainer().remove(key);
    }

    /**
     * Removes attributes from all players marked by this event.
     */
    public void removeAttributesFromAllMarkedPlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            if (isAttributePlayerMarked(player)) {
                removeAttributeModifiersFromPlayer(player);
            }
        }
    }

    /**
     * Gets a copy of all registered attribute modifiers.
     * @return Map of attributes to their corresponding modifiers
     */
    public Map<Attribute, List<AttributeModifier>> getAllAttributeModifiers() {
        return new HashMap<>(attributeModifiers);
    }
}
