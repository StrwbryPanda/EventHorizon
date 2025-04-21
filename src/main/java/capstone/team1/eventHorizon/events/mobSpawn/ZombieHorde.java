package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

/**
 * Event that spawns a horde of zombies near players.
 * This event is classified as a negative event and creates a group of hostile zombies
 * that target the nearest player. The zombies spawn at random locations and cannot
 * spawn in water or lava.
 */
public class ZombieHorde extends BaseMobSpawn {

    /**
     * Constructs a new ZombieHorde event with specific spawn settings.
     * Initializes the event with:
     * - 10 zombies per spawn
     * - Spawn radius between 3 and 30 blocks horizontally
     * - Vertical spawn range between 3 and 20 blocks
     * - Individual spawning with 2-block spacing
     * - Can spawn in air or on surfaces
     */
    public ZombieHorde() {
        super(EntityType.ZOMBIE, EventClassification.NEGATIVE, "zombieHorde");
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
                .setSpawnInterval(60);

    }
    /**
     * Handles post-spawn setup for the zombies.
     * Sets each spawned zombie to target the nearest player.
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
     * Executes the zombie horde spawn event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the zombie horde event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}