package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LifestealOnly extends AttributesBase implements Listener {

    public LifestealOnly() {
        super(EventClassification.NEGATIVE, "lifeStealOnly");
    }

    @Override
    public void applyEffect(Player player) {
        player.sendMessage("You can only regenerate health by attacking mobs!");
    }

    @Override
    public void execute() {
        super.execute(); // Applies the effect to all players
        plugin.getServer().getPluginManager().registerEvents(this, plugin); // Register event listener
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity entity = event.getEntity();

            if (entity instanceof LivingEntity) {
                double healAmount = event.getFinalDamage() * 0.5; // Heal for 50% of damage dealt
                double newHealth = Math.min(player.getHealth() + healAmount, player.getMaxHealth());
                player.setHealth(newHealth);
                player.sendMessage("You stole health from your enemy!");
            }
        }
    }
    public void terminate(){};

}
