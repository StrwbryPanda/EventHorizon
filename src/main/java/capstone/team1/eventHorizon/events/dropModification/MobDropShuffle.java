package capstone.team1.eventHorizon.events.dropModification;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Event that implements a mob drop shuffling mechanic.
 * When active, all mobs will drop random items from the survival item pool
 * instead of their normal drops. Extends BaseDropModification to handle
 * the drop modification logic.
 */
public class MobDropShuffle extends BaseDropModification {
    /**
     * Constructs a new MobDropShuffle event with NEUTRAL classification.
     * Initializes the event with the name "mobDropShuffle".
     */
    public MobDropShuffle() {
        super(EventClassification.NEUTRAL, "mobDropShuffle");
    }

    /**
     * Sets up the drop modifications by generating a pool of all possible survival items
     * and assigning random drops to every spawnable living entity type.
     */
    @Override
    protected void setupDropModifications() {
        List<ItemStack> survivalDropPool = generateSurvivalDropsList();

        for (EntityType entityType : EntityType.values()) {
            if (entityType.isAlive() && entityType.isSpawnable()) {
                setFixedMobDrop(entityType, survivalDropPool);
            }
        }
    }

    /**
     * Executes the mob drop shuffle event by setting up modifications
     * and registering event listeners. Delegates to parent class.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the mob drop shuffle event by unregistering listeners
     * and clearing modifications. Delegates to parent class.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}