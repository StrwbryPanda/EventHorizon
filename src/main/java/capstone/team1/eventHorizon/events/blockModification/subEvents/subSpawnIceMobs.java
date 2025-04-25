package capstone.team1.eventHorizon.events.blockModification.subEvents;

import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.mobSpawn.BaseMobSpawn;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class subSpawnIceMobs extends BaseMobSpawn
{
    private static final List<EntityType> ICE_MOBS = Arrays.asList(
            EntityType.POLAR_BEAR,
            EntityType.SNOW_GOLEM
    );

    public subSpawnIceMobs() {
        super(ICE_MOBS, EventClassification.NEUTRAL, "subSpawnIceMobs");
        setMobCount(30)
                .setMaxSpawnRadius(30)
                .setMinSpawnRadius(5)
                .setMaxYRadius(20)
                .setMinYRadius(5)
                .setMaxSpawnAttempts(20)
                .setHeightClearance(2)
                .setWidthClearance(1)
                .setSurfaceOnlySpawning(false)
                .setAllowWaterSpawns(false)
                .setAllowLavaSpawns(false)
                .setUseGroupSpawning(false)
                .setGroupSpacing(2)
                .setUseContinuousSpawning(false)
                .setSpawnInterval(60)
                .setRandomMobTypes(true);
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
