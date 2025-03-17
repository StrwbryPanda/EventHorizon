package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class LifestealOnly extends BaseAttribute implements Listener {

    private final Plugin plugin;

    public LifestealOnly() {
        super(EventClassification.NEGATIVE, "lifeStealOnly");
        this.plugin = super.plugin;
        addAttributeModifier(Attribute.MAX_HEALTH, -4.0, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void execute() {
        super.execute();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
