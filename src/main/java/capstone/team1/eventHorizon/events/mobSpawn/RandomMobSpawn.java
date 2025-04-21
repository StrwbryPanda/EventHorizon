package capstone.team1.eventHorizon.events.mobSpawn;


import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Event that spawns random mobs near players.
 * This event is classified as neutral and spawns various types of mobs
 * excluding players and ender dragons. Spawns mobs individually at random
 * locations rather than in groups.
 */
public class RandomMobSpawn extends BaseMobSpawn {
    /**
     * Constructs a new RandomMobSpawn event with specific spawn settings.
     * Initializes the event with:
     * - 10 random mobs per spawn
     * - Spawn radius between 3 and 30 blocks horizontally
     * - Vertical spawn range between 3 and 20 blocks
     * - Individual spawning with 2-block spacing
     * - Random mob type selection from all available living entities
     */
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

    /**
     * Executes the random mob spawn event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the random mob spawn event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}