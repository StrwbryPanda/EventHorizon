package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.EntityType;

/**
 * Event that spawns a group of chickens near players.
 * This event is classified as a positive event and creates a flock of chickens
 * in a group formation. The chickens are spawned on solid ground only and
 * cannot spawn in water or lava.
 */
public class ChickenFlock extends BaseMobSpawn {

    /**
     * Constructs a new ChickenFlock event with specific spawn settings.
     * Initializes the event with:
     * - 5 chickens per spawn
     * - Spawn radius between 3 and 30 blocks horizontally
     * - Vertical spawn range between 3 and 20 blocks
     * - Group spawning enabled with 2-block spacing
     * - Surface spawning allowed on solid ground only
     */
    public ChickenFlock() {
        super(EntityType.CHICKEN, EventClassification.POSITIVE, "chickenFlock");
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

    /**
     * Executes the chicken flock spawn event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the chicken flock event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
