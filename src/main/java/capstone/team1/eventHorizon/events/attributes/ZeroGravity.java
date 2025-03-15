package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZeroGravity extends AttributesBase {

    public ZeroGravity() {
        super(EventClassification.NEUTRAL, "zeroGravity");
    }

    @Override
    public void applyEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 1));
        player.sendMessage("You feel weightless as gravity fades away!");
    }

    /**
     * Applies zero gravity to all entities nearby
     */
    public void applyZeroGravityToEntities() {
        for (Entity entity : plugin.getServer().getWorlds().get(0).getEntities()) {
            if (!(entity instanceof Player)) {
                entity.setGravity(false);
            }
        }
    }

    @Override
    public void execute() {
        super.execute();
        applyZeroGravityToEntities();
    }
    public void terminate(){};

}
