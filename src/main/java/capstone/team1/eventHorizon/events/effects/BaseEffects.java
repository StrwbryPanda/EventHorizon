package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    public PotionEffectType effectType = PotionEffectType.SPEED;
    public int duration = DEFAULT_DURATION;
    public int amplifier = DEFAULT_AMPLIFIER;
    public boolean ambient = DEFAULT_AMBIENT;
    public boolean showParticles = DEFAULT_SHOW_PARTICLES;
    public boolean showIcon = DEFAULT_SHOW_ICON;

    // Constructors
    public BaseEffects(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseEffects(PotionEffectType effectType, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.plugin = EventHorizon.plugin;
        this.effectType = effectType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseEffects(PotionEffectType effectType, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.plugin;
        this.effectType = effectType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    // Executes the event
    @Override
    public void execute() {
        applyPotionEffectToAllPlayers();
    }

    //  stops the event
    @Override
    public void terminate() {
        removePotionEffectFromAllPlayers();
    }

    public void applyPotionEffectToAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            applyPotionEffectToPlayer(player);
        }
    }

    public void applyPotionEffectToPlayer(Player player) {
        PotionEffect effect = new PotionEffect(
                effectType,
                duration,
                amplifier,
                ambient,
                showParticles,
                showIcon
        );
        player.addPotionEffect(effect);
    }

    public void removePotionEffectFromAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removePotionEffectFromPlayer(player);
        }
    }

    public void removePotionEffectFromPlayer(Player player) {
        if (effectType != null && player.hasPotionEffect(effectType)) {
            player.removePotionEffect(effectType);
        }
    }

//    public void markPotionEffectPlayer(Player player) {
//
//    }
//
//    public boolean isPotionEffectPlayerMarked(Player player) {
//
//    }

    // Setters
    public BaseEffects setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public BaseEffects setAmplifier(int amplifier) {
        this.amplifier = amplifier;
        return this;
    }

    public BaseEffects setAmbient(boolean ambient) {
        this.ambient = ambient;
        return this;
    }

    public BaseEffects setShowParticles(boolean showParticles) {
        this.showParticles = showParticles;
        return this;
    }

    public BaseEffects setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
        return this;
    }
    public void terminate(){};

}

//remove start/stop --> execute/terminate
//add other attributes
//getter/setter for globalAmplifier and globalDuration
//change 999 to infinite/max_value
//remove potion effect in terminate
//move logic to parent and feed stuff in constructor
//method to add potion effect to one player
//method to call ^ for all players
//same for remove
