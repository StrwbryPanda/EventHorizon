package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

//Players can now mine twice as fast
public class Overmine extends BaseEffects{
    public Overmine() {
        super(EventClassification.POSITIVE, "overmine");
    }

    @Override
    public void start() {
        // Loop through all players and give potion
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, globalDuration, globalAmplifier));
        }

        // Schedule the end method after globalDuration ticks
        plugin.getServer().getScheduler().runTaskLater(plugin, this::end, globalDuration);
    }

    @Override
    public void end() {
        // Loop through all players again and remove potion
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.removePotionEffect(PotionEffectType.HASTE);
        }
    }
    public void terminate(){};

}
