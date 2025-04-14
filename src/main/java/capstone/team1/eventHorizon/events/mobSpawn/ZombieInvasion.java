package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

/**
 * Event that continuously spawns a zombie near players
 */
public class ZombieInvasion extends BaseMobSpawn {

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

    @Override
    protected void onMobSpawned(Entity entity, Player player) {
        if (entity instanceof Zombie zombie) {
            zombie.setTarget(player);
        }
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
