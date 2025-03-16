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

        addAttributeModifier(Attribute.SCALE, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.MAX_HEALTH, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.ATTACK_DAMAGE, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.KNOCKBACK_RESISTANCE, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.MOVEMENT_SPEED, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.SNEAKING_SPEED, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.WATER_MOVEMENT_EFFICIENCY, 2, AttributeModifier.Operation.ADD_NUMBER);


        addAttributeModifier(Attribute.STEP_HEIGHT, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.JUMP_STRENGTH, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.SAFE_FALL_DISTANCE, 2, AttributeModifier.Operation.ADD_NUMBER);

        addAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, 2, AttributeModifier.Operation.ADD_NUMBER);
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
