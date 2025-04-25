package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

/**
 *  Event that spawns a group of end themed mobs near players.
 */
public class EndRaid extends BaseMobSpawn {
    private static final List<EntityType> END_MOBS = Arrays.asList(
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.SHULKER,
            EntityType.END_CRYSTAL,
            EntityType.PHANTOM
    );

    public EndRaid() {
        super(END_MOBS, EventClassification.NEGATIVE, "endRaid");
        setMobCount(15)
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
     * Executes the end raid event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the end raid event.
     * Delegates to the parent class implementation.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
