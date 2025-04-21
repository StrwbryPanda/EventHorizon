package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * Event that spawns a Creeper near players.
 * This event is classified as a negative event and spawns a single creeper
 * that targets the nearest player. The creeper spawns from above and cannot
 * spawn in water or lava.
 */
public class DropCreeper extends BaseMobSpawn {

    /**
     * Constructs a new DropCreeper event with specific spawn settings.
     * Initializes the event with:
     * - 1 creeper per spawn
     * - Spawn radius between 3 and 10 blocks horizontally
     * - Vertical spawn range between 3 and 30 blocks
     * - Individual spawning with 2-block spacing
     * - Can spawn in air or on surfaces
     */
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

    /**
     * Handles post-spawn setup for the creeper.
     * Sets the spawned creeper to target the nearest player.
     *
     * @param entity The spawned entity (creeper)
     * @param player The nearest player to target
     */
    @Override
    protected void onMobSpawned(Entity entity, Player player) {
        if (entity instanceof Creeper creeper) {
            creeper.setTarget(player);
        }
    }

    /**
     * Executes the creeper spawn event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the creeper spawn event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
