package capstone.team1.eventHorizon.events.mobSpawn;


import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Event that spawns random mobs near players
 */
public class RandomMobSpawn extends BaseMobSpawn {
    public RandomMobSpawn() {
        super(EntityType.ZOMBIE, EventClassification.NEUTRAL, "randomMobSpawn");
        setMobCount(10)
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
                .setUseGroupSpawning(false)
                .setGroupSpacing(2)
                .setUseContinuousSpawning(false)
                .setSpawnInterval(60)
                .setRandomMobTypes(true)
                .setMobTypes(Arrays.stream(EntityType.values())
                    .filter(EntityType::isAlive)
                    .filter(type -> !type.equals(EntityType.PLAYER)
                        && !type.equals(EntityType.ENDER_DRAGON))
                    .collect(Collectors.toList()));
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