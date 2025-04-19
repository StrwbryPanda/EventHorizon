package capstone.team1.eventHorizon.events.dropModification;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Event that shuffles mob drops to random fixed items
 */
public class MobDropShuffle extends BaseDropModification {
    public MobDropShuffle() {
        super(EventClassification.NEUTRAL, "mobDropShuffle");
    }

    @Override
    protected void setupDropModifications() {
        List<ItemStack> survivalDropPool = generateSurvivalDropsList();

        for (EntityType entityType : EntityType.values()) {
            if (entityType.isAlive() && entityType.isSpawnable()) {
                setFixedMobDrop(entityType, survivalDropPool);
            }
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