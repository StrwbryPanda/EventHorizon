package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class HoneyIShrunkTheKids extends BaseAttribute {

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

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
    }
}
