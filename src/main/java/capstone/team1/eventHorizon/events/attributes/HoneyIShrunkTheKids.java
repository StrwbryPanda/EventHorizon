package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.UUID;

public class HoneyIShrunkTheKids extends BaseAttribute {

    public HoneyIShrunkTheKids() {
        super(EventClassification.NEGATIVE, "honeyIShrunkTheKids");

        addAttributeModifier(Attribute.MAX_HEALTH, -2.0, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.MOVEMENT_SPEED, -2.0, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.ATTACK_DAMAGE, -2.0, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.SCALE, -2.0, AttributeModifier.Operation.ADD_NUMBER);

        addAttributeModifier(Attribute.SNEAKING_SPEED, -2.0, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.WATER_MOVEMENT_EFFICIENCY, -2.0, AttributeModifier.Operation.ADD_NUMBER);


        addAttributeModifier(Attribute.STEP_HEIGHT, -2.0, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.JUMP_STRENGTH, -2.0, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.SAFE_FALL_DISTANCE, -2.0, AttributeModifier.Operation.ADD_NUMBER);

        addAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, -2.0, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, -2.0, AttributeModifier.Operation.ADD_NUMBER);

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
