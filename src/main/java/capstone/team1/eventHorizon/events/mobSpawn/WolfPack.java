package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

/**
 * Event that spawns a pack of angry wolves near players.
 * This event is classified as a negative event and creates a pack of hostile wolves
 * that target the nearest player. The wolves spawn in a group formation and cannot
 * spawn in water or lava.
 */
public class WolfPack extends BaseMobSpawn {
    /**
     * Constructs a new WolfPack event with specific spawn settings.
     * Initializes the event with:
     * - 5 wolves per spawn
     * - Spawn radius between 3 and 30 blocks horizontally
     * - Vertical spawn range between 3 and 20 blocks
     * - Group spawning enabled with 2-block spacing
     * - Surface spawning allowed on solid ground only
     */
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

    /**
     * Handles post-spawn setup for the wolves.
     * Sets each spawned wolf to angry state and targets the nearest player.
     *
     * @param entity The spawned entity (wolf)
     * @param player The nearest player to target
     */
    @Override
    protected void onMobSpawned(Entity entity, Player player) {
        if (entity instanceof Wolf wolf) {
            wolf.setAngry(true);
            wolf.setTarget(player);
        }
    }

    /**
     * Executes the wolf pack spawn event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the wolf pack event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}