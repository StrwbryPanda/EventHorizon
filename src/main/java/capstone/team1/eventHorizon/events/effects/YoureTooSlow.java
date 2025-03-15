package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

//Player receives slowness effect
public class YoureTooSlow extends BaseEffects{
    public YoureTooSlow() {
        super(EventClassification.NEGATIVE, "youreTooSlow");
    }

    @Override
    public void start() {
        // Loop through all players and give potion
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, globalDuration, globalAmplifier));
        }

        // Schedule the end method after globalDuration ticks
        plugin.getServer().getScheduler().runTaskLater(plugin, this::end, globalDuration);
    }

    @Override
    public void end() {
        // Loop through all players again and remove potion
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.removePotionEffect(PotionEffectType.SLOWNESS);
        }
    }
    public void terminate(){};

}
