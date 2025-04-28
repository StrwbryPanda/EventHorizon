package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract base class for managing potion effects in the EventHorizon plugin.
 * Handles the application and removal of potion effects to players.
 */
public abstract class BaseEffects extends BaseEvent {
    /**
     * Reference to the main plugin instance
     */
    EventHorizon plugin = EventHorizon.getPlugin();
    /**
     * Random number generator for effect-related operations
     */
    protected final Random random = new Random();
    /**
     * Unique identifier key for tracking affected players
     */
    protected final NamespacedKey key;

    // Default configuration values
    /**
     * Default duration for potion effects in ticks (30 seconds).
     */
    private static final int DEFAULT_DURATION = 600; // 30 seconds (in ticks)
    /**
     * Default amplifier level for potion effects (Level 1).
     */
    private static final int DEFAULT_AMPLIFIER = 0; // Level 1
    /**
     * Default setting for whether the effect particles should be ambient.
     */
    private static final boolean DEFAULT_AMBIENT = false;
    /**
     * Default setting for whether to show effect particles.
     */
    private static final boolean DEFAULT_SHOW_PARTICLES = true;
    /**
     * Default setting for whether to show the effect icon in the player's inventory.
     */
    private static final boolean DEFAULT_SHOW_ICON = true;

    /**
     * List of potion effects to be applied by this effect event.
     * Contains all PotionEffect instances that will be applied to players.
     */
    protected List<PotionEffect> effects = new ArrayList<>();

    // Constructors
    /**
     * Constructs a BaseEffects instance with the specified classification and event name.
     * Creates a namespaced key using the event name for tracking affected players.
     *
     * @param classification the classification of the event (POSITIVE, NEGATIVE, or NEUTRAL)
     * @param eventName unique identifier for this event
     */
    public BaseEffects(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    /**
     * Constructs a BaseEffects instance with a single potion effect and neutral classification.
     * Creates a namespaced key for tracking and adds the specified effect with default parameters.
     *
     * @param effectType the type of potion effect to apply
     * @param eventName unique identifier for this event
     */
    public BaseEffects(PotionEffectType effectType, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);

        addEffect(effectType, DEFAULT_DURATION, DEFAULT_AMPLIFIER, DEFAULT_AMBIENT,
                DEFAULT_SHOW_PARTICLES, DEFAULT_SHOW_ICON);
    }

    /**
     * Constructs a BaseEffects instance with a single potion effect and specified classification.
     * Creates a namespaced key for tracking and adds the specified effect with default parameters.
     *
     * @param effectType the type of potion effect to apply
     * @param classification the classification of the event (POSITIVE, NEGATIVE, or NEUTRAL)
     * @param eventName unique identifier for this event
     */
    public BaseEffects(PotionEffectType effectType, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.key = new NamespacedKey(plugin, this.eventName);

        addEffect(effectType, DEFAULT_DURATION, DEFAULT_AMPLIFIER, DEFAULT_AMBIENT,
                DEFAULT_SHOW_PARTICLES, DEFAULT_SHOW_ICON);
    }

    /**
     * Executes this effect event by applying potion effects to all online players.
     * Logs the execution status.
     */
    @Override
    public void execute() {
        MsgUtility.log("<green>Executing effect event: " + this.eventName);
        applyPotionEffectsToAllPlayers();
    }

    /**
     * Terminates this effect event by removing potion effects from all affected players.
     * Logs the termination status.
     */
    @Override
    public void terminate() {
        MsgUtility.log("<red>Terminating effect event: " + this.eventName);
        removePotionEffectsFromAllPlayers();
    }

    /**
     * Applies all registered potion effects to all online players.
     * Logs the success rate of effect application.
     */
    public void applyPotionEffectsToAllPlayers() {
        int successCount = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            try {
                applyPotionEffectsToPlayer(player);
                successCount++;
            } catch (Exception e) {
                MsgUtility.warning("Failed to apply effects to player " + player.getName() + ": " + e.getMessage());
            }
        }
        MsgUtility.log("Applied potion effects to " + successCount + "/" + players.size() + " players for event: " + this.eventName);
    }

    /**
     * Applies all registered potion effects to a specific player.
     * Marks the player as affected by this effect event.
     *
     * @param player the player to receive the effects
     */
    public void applyPotionEffectsToPlayer(Player player) {
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }
        markEffectPlayer(player);
    }

    /**
     * Removes all registered potion effects from all online players.
     * Logs the success rate of effect removal.
     */
    public void removePotionEffectsFromAllPlayers() {
        int successCount = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            try {
                removePotionEffectsFromPlayer(player);
                successCount++;
            } catch (Exception e) {
                MsgUtility.warning("Failed to remove effects from player " + player.getName() + ": " + e.getMessage());
            }
        }
        MsgUtility.log("Removed potion effects from " + successCount + "/" + players.size() + " players for event: " + this.eventName);
    }

    /**
     * Removes all registered potion effects from a specific player.
     * Unmarks the player from this effect event.
     *
     * @param player the player to remove effects from
     */
    public void removePotionEffectsFromPlayer(Player player) {
        for (PotionEffect effect : effects) {
            PotionEffectType effectType = effect.getType();
            if (player.hasPotionEffect(effectType)) {
                player.removePotionEffect(effectType);
            }
        }
        unmarkEffectPlayer(player);
    }

    // Methods to add and remove effects
    /**
     * Adds a new potion effect with specified parameters to this effect event.
     *
     * @param effectType the type of potion effect
     * @param duration effect duration in ticks
     * @param amplifier effect strength level
     * @param ambient whether the effect is ambient
     * @param showParticles whether to show effect particles
     * @param showIcon whether to show the effect icon
     * @return this BaseEffects instance for method chaining
     */
    public BaseEffects addEffect(PotionEffectType effectType, int duration, int amplifier,
                                 boolean ambient, boolean showParticles, boolean showIcon) {
        PotionEffect effect = new PotionEffect(
                effectType,
                duration,
                amplifier,
                ambient,
                showParticles,
                showIcon
        );
        effects.add(effect);
        return this;
    }

    /**
     * Adds a new potion effect with default parameters.
     *
     * @param effectType the type of potion effect to add
     * @return this BaseEffects instance for method chaining
     */
    public BaseEffects addEffect(PotionEffectType effectType) {
        return addEffect(effectType, DEFAULT_DURATION, DEFAULT_AMPLIFIER, DEFAULT_AMBIENT,
                DEFAULT_SHOW_PARTICLES, DEFAULT_SHOW_ICON);
    }

    /**
     * Adds a pre-configured potion effect to this effect event.
     *
     * @param effect the PotionEffect to add
     * @return this BaseEffects instance for method chaining
     */
    public BaseEffects addEffect(PotionEffect effect) {
        effects.add(effect);
        return this;
    }

    /**
     * Removes a specific type of potion effect from this effect event.
     *
     * @param effectType the type of effect to remove
     * @return this BaseEffects instance for method chaining
     */
    public BaseEffects removeEffect(PotionEffectType effectType) {
        effects.removeIf(effect -> effect.getType().equals(effectType));
        return this;
    }

    /**
     * Retrieves a specific potion effect from this effect event.
     *
     * @param effectType the type of effect to retrieve
     * @return the PotionEffect if found, null otherwise
     */
    public PotionEffect getEffect(PotionEffectType effectType) {
        for (PotionEffect effect : effects) {
            if (effect.getType().equals(effectType)) {
                return effect;
            }
        }
        return null;
    }

    /**
     * Checks if this effect event contains a specific type of potion effect.
     *
     * @param effectType the type of effect to check for
     * @return true if the effect exists, false otherwise
     */
    public boolean hasEffect(PotionEffectType effectType) {
        return getEffect(effectType) != null;
    }

    /**
     * Marks a player as affected by this effect event using persistent data.
     *
     * @param player the player to mark
     */
    public void markEffectPlayer(Player player) {
        player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    /**
     * Checks if a player is marked as affected by this effect event.
     *
     * @param player the player to check
     * @return true if the player is marked, false otherwise
     */
    public boolean isEffectPlayerMarked(Player player) {
        return player.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    /**
     * Removes the effect event marker from a player.
     *
     * @param player the player to unmark
     */
    public void unmarkEffectPlayer(Player player) {
        player.getPersistentDataContainer().remove(key);
    }

    /**
     * Removes all effects from players who are marked as affected by this event.
     */
    public void removeEffectsFromAllMarkedPlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            if (isEffectPlayerMarked(player)) {
                removePotionEffectsFromPlayer(player);
            }
        }
    }

    /**
     * Gets a copy of all potion effects registered in this effect event.
     *
     * @return a new list containing all registered PotionEffects
     */
    public List<PotionEffect> getAllEffects() {
        return new ArrayList<>(effects);
    }
}