package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GrowthSpurt extends AttributesBase {

    public GrowthSpurt(JavaPlugin plugin) {
        super(EventClassification.NEUTRAL, "growthSpurt");
    }

    @Override
    public void applyEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1));
        Location loc = player.getLocation();
        ArmorStand largeStand = loc.getWorld().spawn(loc, ArmorStand.class);

        largeStand.setCustomName("Giant " + player.getName());
        largeStand.setCustomNameVisible(true);
        largeStand.setGravity(false);
        largeStand.setInvisible(false);
        largeStand.setSmall(false);

        largeStand.addPassenger(player); 

        player.sendMessage("You have grown into a giant!");
    }

    @Override
    public void execute() {
        super.execute(); 
    }
    public void terminate(){};

}
