package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

/**
 * Event that modifies the player's attributes to feel like they've grown
 */
public class GrowthSpurt extends BaseAttribute {
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

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
    }
}
