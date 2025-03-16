package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.utility.MsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BaseEffects extends BaseEvent {
    protected final Plugin plugin;
    protected final Random random = new Random();
    protected final NamespacedKey key;

    // Default configuration values
    private static final int DEFAULT_DURATION = 600; // 30 seconds (in ticks)
    private static final int DEFAULT_AMPLIFIER = 0; // Level 1
    private static final boolean DEFAULT_AMBIENT = false;
    private static final boolean DEFAULT_SHOW_PARTICLES = true;
    private static final boolean DEFAULT_SHOW_ICON = true;

    // Effects properties
    protected List<PotionEffect> effects = new ArrayList<>();

    // Constructors
    public BaseEffects(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseEffects(PotionEffectType effectType, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);

        addEffect(effectType, DEFAULT_DURATION, DEFAULT_AMPLIFIER, DEFAULT_AMBIENT,
                DEFAULT_SHOW_PARTICLES, DEFAULT_SHOW_ICON);
    }

    public BaseEffects(PotionEffectType effectType, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);

        addEffect(effectType, DEFAULT_DURATION, DEFAULT_AMPLIFIER, DEFAULT_AMBIENT,
                DEFAULT_SHOW_PARTICLES, DEFAULT_SHOW_ICON);
    }

    // Executes the event
    @Override
    public void execute() {
        MsgUtil.log("<green>Executing effect event: " + this.eventName);
        applyPotionEffectsToAllPlayers();
    }

    //  Terminates the event
    @Override
    public void terminate() {
        MsgUtil.log("<red>Terminating effect event: " + this.eventName);
        removePotionEffectsFromAllPlayers();
    }

    // Applies potion effects to all players
    public void applyPotionEffectsToAllPlayers() {
        int successCount = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            try {
                applyPotionEffectsToPlayer(player);
                successCount++;
            } catch (Exception e) {
                MsgUtil.warning("Failed to apply effects to player " + player.getName() + ": " + e.getMessage());
            }
        }
        MsgUtil.log("<green>Applied potion effects to " + successCount + "/" + players.size() + " players for event: " + this.eventName);
    }

    // Applies potion effects to a player
    public void applyPotionEffectsToPlayer(Player player) {
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }
        markEffectPlayer(player);
    }

    // Removes potion effects from all players
    public void removePotionEffectsFromAllPlayers() {
        int successCount = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            try {
                removePotionEffectsFromPlayer(player);
                successCount++;
            } catch (Exception e) {
                MsgUtil.warning("Failed to remove effects from player " + player.getName() + ": " + e.getMessage());
            }
        }
        MsgUtil.log("Removed potion effects from " + successCount + "/" + players.size() + " players for event: " + this.eventName);
    }

    // Removes potion effects from a player
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

    public BaseEffects addEffect(PotionEffectType effectType) {
        return addEffect(effectType, DEFAULT_DURATION, DEFAULT_AMPLIFIER, DEFAULT_AMBIENT,
                DEFAULT_SHOW_PARTICLES, DEFAULT_SHOW_ICON);
    }

    public BaseEffects addEffect(PotionEffect effect) {
        effects.add(effect);
        return this;
    }

    public BaseEffects removeEffect(PotionEffectType effectType) {
        effects.removeIf(effect -> effect.getType().equals(effectType));
        return this;
    }

    public PotionEffect getEffect(PotionEffectType effectType) {
        for (PotionEffect effect : effects) {
            if (effect.getType().equals(effectType)) {
                return effect;
            }
        }
        return null;
    }

    public boolean hasEffect(PotionEffectType effectType) {
        return getEffect(effectType) != null;
    }

    public void markEffectPlayer(Player player) {
        player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    public boolean isEffectPlayerMarked(Player player) {
        return player.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    public void unmarkEffectPlayer(Player player) {
        player.getPersistentDataContainer().remove(key);
    }

    public void removeEffectsFromAllMarkedPlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            if (isEffectPlayerMarked(player)) {
                removePotionEffectsFromPlayer(player);
            }
        }
    }

    public List<PotionEffect> getAllEffects() {
        return new ArrayList<>(effects);
    }
}