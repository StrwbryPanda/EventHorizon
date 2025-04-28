package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

/**
 * Represents an event that increases the player's physical attributes, simulating a growth spurt.
 * This event enhances various player capabilities including size, health, damage, movement, and interaction ranges.
 * Extends {@link BaseAttribute} and is classified as a NEUTRAL event.
 */
public class GrowthSpurt extends BaseAttribute {

    /**
     * Constructs a new GrowthSpurt event.
     * Initializes the event with NEUTRAL classification and applies various attribute modifiers:
     * - Doubles player scale
     * - Increases max health by 4 hearts
     * - Increases attack damage by 1
     * - Enhances knockback resistance by 50%
     * - Increases movement speeds by 50%
     * - Increases step height and safe fall distance
     * - Expands interaction ranges by 25%
     */
    public GrowthSpurt() {
        super(EventClassification.NEUTRAL, "growthSpurt");

        addAttributeModifier(Attribute.SCALE, 1, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.MAX_HEALTH, 4, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.ATTACK_DAMAGE, 1, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.KNOCKBACK_RESISTANCE, 0.5, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.MOVEMENT_SPEED, 0.5, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.SNEAKING_SPEED, 0.5, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.WATER_MOVEMENT_EFFICIENCY, 0.5, AttributeModifier.Operation.ADD_SCALAR);

        addAttributeModifier(Attribute.STEP_HEIGHT, 1, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.JUMP_STRENGTH, 0.5, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.SAFE_FALL_DISTANCE, 2, AttributeModifier.Operation.ADD_NUMBER);

        addAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, 0.25, AttributeModifier.Operation.ADD_SCALAR);
        addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, 0.25, AttributeModifier.Operation.ADD_SCALAR);
    }

    /**
     * Executes the growth spurt event by calling the parent's execute method.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the growth spurt event by calling the parent's terminate method.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}