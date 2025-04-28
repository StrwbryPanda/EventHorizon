package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

/**
 * Represents an event that reduces the player's maximum health to half a heart.
 * This event significantly increases the difficulty by making any damage potentially lethal.
 * Extends {@link BaseAttribute} and is classified as a NEGATIVE event.
 */
public class HalfAHeart extends BaseAttribute {

    /**
     * Constructs a new HalfAHeart event.
     * Initializes the event with NEGATIVE classification and reduces player's max health by 19 hearts,
     * effectively leaving them with only half a heart of health (1 health point).
     */
    public HalfAHeart() {
        super(EventClassification.NEGATIVE, "halfAHeart");
        addAttributeModifier(Attribute.MAX_HEALTH, -19.0, AttributeModifier.Operation.ADD_NUMBER);
    }

    /**
     * Executes the half heart event by calling the parent's execute method.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the half heart event by calling the parent's terminate method.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}