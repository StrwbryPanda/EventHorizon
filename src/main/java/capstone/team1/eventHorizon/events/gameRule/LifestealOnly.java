package capstone.team1.eventHorizon.events.gameRule;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.attributes.BaseAttribute;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/** Class is WIP */
public class LifestealOnly<Boolean> extends BaseGameRule<Boolean> implements Listener {

    public LifestealOnly() {
        super(EventClassification.NEGATIVE, "lifeStealOnly");
    }

    @Override
    public void execute() {
        super.execute();
        EventHorizon.getPlugin().getServer().getPluginManager().registerEvents(this, EventHorizon.getPlugin());
    }

    @Override
    public void terminate() {
        super.terminate();
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity entity = event.getEntity();

            if (entity instanceof LivingEntity) {
                double healAmount = event.getFinalDamage() * 0.5;
                double newHealth = Math.min(player.getHealth() + healAmount, player.getAttribute(Attribute.MAX_HEALTH).getValue());
                player.setHealth(newHealth);
            }
        }
    }
}
