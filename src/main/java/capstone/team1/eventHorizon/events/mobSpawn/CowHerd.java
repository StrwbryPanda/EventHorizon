package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.EntityType;

/**
 * Event that spawns a group of cows near players.
 * This event is classified as a positive event and creates a herd of cows
 * in a group formation. The cows are spawned on solid ground only and
 * cannot spawn in water or lava.
 */
public class CowHerd extends BaseMobSpawn {

    /**
     * Constructs a new CowHerd event with specific spawn settings.
     * Initializes the event with:
     * - 5 cows per spawn
     * - Spawn radius between 3 and 30 blocks horizontally
     * - Vertical spawn range between 3 and 20 blocks
     * - Group spawning enabled with 2-block spacing
     * - Surface spawning allowed on solid ground only
     */
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

    /**
     * Executes the cow herd spawn event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the cow herd event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
