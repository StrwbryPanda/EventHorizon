package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Player;

/**
 * Represents an event that puts players into a fasting state.
 * This event sets players' food level to zero, causing hunger effects.
 * Extends {@link BaseAttribute} and is classified as a NEGATIVE event.
 */
public class Fasting extends BaseAttribute {

    /**
     * Constructs a new Fasting event.
     * Initializes the event with NEGATIVE classification.
     */
    public Fasting() {
        super(EventClassification.NEGATIVE, "fasting");
    }

    /**
     * Applies the fasting effect to a player by setting their food level to zero.
     * @param player The player to apply the fasting effect to
     */
    @Override
    public void applyAttributeModifiersToPlayer(Player player) {
        super.applyAttributeModifiersToPlayer(player);
        player.setFoodLevel(0);
    }

    /**
     * Executes the fasting event by calling the parent's execute method.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the fasting event by calling the parent's terminate method.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}