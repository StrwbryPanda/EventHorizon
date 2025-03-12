package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that spawns a pack of angry wolves near players
 */
public class WolfPack extends BaseMobSpawn {

    public WolfPack() {
        super(EntityType.WOLF, EventClassification.NEGATIVE, "wolfPack");
            setMobCount(5)
                    .setMaxSpawnRadius(30)
                    .setMinSpawnRadius(3)
                    .setMaxSpawnAttempts(20)
                    .setSurfaceOnlySpawning(false)
                    .setAllowWaterSpawns(false)
                    .setAllowLavaSpawns(false)
                    .setHeightClearance(1)
                    .setWidthClearance(1)
                    .setGroupSpawning(true)
                    .setGroupSpacing(2);
    }

    @Override
    protected void onMobSpawned(Entity entity, Player player) {
        if (entity instanceof Wolf wolf) {
            wolf.setAngry(true);
            wolf.setTarget(player);
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999, 1));
        }
    }

    @Override
    public void execute() {
        super.execute();
    }

    public void stopEvent() {
        //super.stopEvent();
        killAllSpawnedMob();
    }
}