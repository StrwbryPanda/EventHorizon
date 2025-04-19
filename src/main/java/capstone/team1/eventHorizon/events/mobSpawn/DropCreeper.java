package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * Event that spawns a Creeper near players
 */
public class DropCreeper extends BaseMobSpawn {

    public DropCreeper() {
        super(EntityType.CREEPER, EventClassification.NEGATIVE, "dropCreeper");
        setMobCount(1)
                .setMaxSpawnRadius(10)
                .setMinSpawnRadius(3)
                .setMaxYRadius(30)
                .setMinYRadius(3)
                .setMaxSpawnAttempts(20)
                .setHeightClearance(2)
                .setWidthClearance(1)
                .setSurfaceOnlySpawning(false)
                .setAllowWaterSpawns(false)
                .setAllowLavaSpawns(false)
                .setUseGroupSpawning(false)
                .setGroupSpacing(2)
                .setUseContinuousSpawning(false)
                .setSpawnInterval(60);

    }

    @Override
    protected void onMobSpawned(Entity entity, Player player) {
        if (entity instanceof Creeper creeper) {
            creeper.setTarget(player);
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
