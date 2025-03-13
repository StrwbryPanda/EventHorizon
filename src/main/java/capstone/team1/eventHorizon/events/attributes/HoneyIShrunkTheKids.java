package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HoneyIShrunkTheKids extends AttributesBase {

    public HoneyIShrunkTheKids() {
        super(EventClassification.NEGATIVE, "honeyIShrunkTheKids");
    }

    @Override
    public void applyEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1));

        Location loc = player.getLocation();
        ArmorStand smallStand = loc.getWorld().spawn(loc, ArmorStand.class);

        smallStand.setSmall(true);
        smallStand.setInvisible(false);
        smallStand.setGravity(false);

        smallStand.addPassenger(player);

        player.sendMessage("You have shrunk in size!");

    }
    @Override
    public void execute() {
        super.execute(); 
    }

}
