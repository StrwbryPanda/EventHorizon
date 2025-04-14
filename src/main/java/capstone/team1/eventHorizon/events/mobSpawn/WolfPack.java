package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

/**
 * Event that spawns a pack of angry wolves near players
 */
public class WolfPack extends BaseMobSpawn {
    public WolfPack() {
        super(EntityType.WOLF, EventClassification.NEGATIVE, "wolfPack");
        setMobCount(5)
                .setMaxSpawnRadius(30)
                .setMinSpawnRadius(3)
                .setMaxYRadius(20)
                .setMinYRadius(3)
                .setMaxSpawnAttempts(20)
                .setHeightClearance(1)
                .setWidthClearance(1)
                .setSurfaceOnlySpawning(false)
                .setAllowWaterSpawns(false)
                .setAllowLavaSpawns(false)
                .setUseGroupSpawning(true)
                .setGroupSpacing(2)
                .setUseContinuousSpawning(false)
                .setSpawnInterval(60);

    }

    @Override
    protected void onMobSpawned(Entity entity, Player player) {
        if (entity instanceof Wolf wolf) {
            wolf.setAngry(true);
            wolf.setTarget(player);
        }
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
    }
}