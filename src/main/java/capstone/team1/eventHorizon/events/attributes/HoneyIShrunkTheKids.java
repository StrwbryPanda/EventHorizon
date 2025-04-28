package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

/**
 * Represents an event that shrinks players, similar to the movie "Honey, I Shrunk the Kids".
 * This event reduces player size, health, and attack damage while increasing movement capabilities.
 * Extends {@link BaseAttribute} and is classified as a NEGATIVE event.
 */
public class HoneyIShrunkTheKids extends BaseAttribute {

    /**
     * Constructs a new HoneyIShrunkTheKids event.
     * Initializes the event with NEGATIVE classification and applies various attribute modifiers:
     * - Reduces player scale by 75%
     * - Decreases max health by 2 hearts
     * - Increases movement speed by 25%
     * - Reduces attack damage by 25%
     * - Enhances sneaking, swimming, and jumping abilities
     * - Slightly increases safe fall distance
     */
    public HoneyIShrunkTheKids() {
        super(EventClassification.NEGATIVE, "honeyIShrunkTheKids");

        addAttributeModifier(Attribute.SCALE, -0.75, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.MAX_HEALTH, -2.0, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.MOVEMENT_SPEED, 0.25, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.ATTACK_DAMAGE, -0.25, AttributeModifier.Operation.ADD_SCALAR);

        addAttributeModifier(Attribute.SNEAKING_SPEED, 0.25, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.WATER_MOVEMENT_EFFICIENCY, 0.25, AttributeModifier.Operation.ADD_SCALAR);

        addAttributeModifier(Attribute.JUMP_STRENGTH, 0.25, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.SAFE_FALL_DISTANCE, 1.0, AttributeModifier.Operation.ADD_NUMBER);

    }

    /**
     * Executes the shrinking event by calling the parent's execute method.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the shrinking event by calling the parent's terminate method.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
