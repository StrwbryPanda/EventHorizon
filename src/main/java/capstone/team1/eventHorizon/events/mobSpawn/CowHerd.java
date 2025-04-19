package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.EntityType;

/**
 * Event that spawns a group of cows near players
 */
public class CowHerd extends BaseMobSpawn {

    public CowHerd() {
        super(EntityType.COW, EventClassification.POSITIVE, "cowHerd");
        setMobCount(5)
                .setMaxSpawnRadius(30)
                .setMinSpawnRadius(3)
                .setMaxYRadius(20)
                .setMinYRadius(3)
                .setMaxSpawnAttempts(20)
                .setHeightClearance(2)
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
    public void execute() {
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
    }
}
