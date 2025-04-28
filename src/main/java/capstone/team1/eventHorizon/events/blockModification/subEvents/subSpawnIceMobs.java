package capstone.team1.eventHorizon.events.blockModification.subEvents;

import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.mobSpawn.BaseMobSpawn;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

/**
 * A mob spawning event that spawns ice-themed entities like Polar Bears and Snow Golems.
 * This event is classified as neutral and provides controlled spawning of ice-related mobs
 * within specified parameters.
 * Extends BaseMobSpawn to utilize mob spawning functionality.
 */
public class subSpawnIceMobs extends BaseMobSpawn
{
    /**
     * List of ice-themed mob types that can be spawned by this event.
     * Currently includes Polar Bears and Snow Golems.
     */
    private static final List<EntityType> ICE_MOBS = Arrays.asList(
            EntityType.POLAR_BEAR,
            EntityType.SNOW_GOLEM
    );

    /**
     * Constructs a new subSpawnIceMobs event.
     * Initializes the event with specific spawning parameters:
     * - 30 mobs to spawn
     * - Spawn radius between 5 and 30 blocks
     * - Vertical spawn range of 5 to 20 blocks
     * - 20 maximum spawn attempts
     * - Clearance requirements of 2 blocks height and 1 block width
     * - Disabled water and lava spawns
     * - Individual spawning (no groups)
     * - One-time spawning (non-continuous)
     * - Random selection from available mob types
     */
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

    /**
     * Executes the ice mob spawning event using the configured parameters.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the spawning event and performs necessary cleanup.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
