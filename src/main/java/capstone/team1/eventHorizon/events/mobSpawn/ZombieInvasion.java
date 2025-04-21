package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

/**
 * Event that continuously spawns a zombie near players.
 * This event is classified as a negative event and creates individual zombies
 * that target the nearest player. The zombies spawn continuously at a fixed interval
 * and cannot spawn in water or lava.
 */
public class ZombieInvasion extends BaseMobSpawn {

    /**
     * Constructs a new ZombieInvasion event with specific spawn settings.
     * Initializes the event with:
     * - 1 zombie per spawn
     * - Spawn radius between 3 and 30 blocks horizontally
     * - Vertical spawn range between 3 and 20 blocks
     * - Continuous spawning every 20 ticks
     * - Individual spawning with 2-block spacing
     * - Can spawn in air or on surfaces
     */
    public ZombieInvasion() {
        super(EntityType.ZOMBIE, EventClassification.NEGATIVE, "zombieInvasion");
        setMobCount(1)
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
                .setUseContinuousSpawning(true)
                .setSpawnInterval(20);

    }

    /**
     * Handles post-spawn setup for the zombie.
     * Sets the spawned zombie to target the nearest player.
     *
     * @param entity The spawned entity (zombie)
     * @param player The nearest player to target
     */
    @Override
    protected void onMobSpawned(Entity entity, Player player) {
        if (entity instanceof Zombie zombie) {
            zombie.setTarget(player);
        }
    }

    /**
     * Executes the zombie invasion spawn event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the zombie invasion event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
