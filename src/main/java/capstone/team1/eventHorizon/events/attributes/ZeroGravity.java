package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZeroGravity extends BaseAttribute {

    public ZeroGravity() {
        super(EventClassification.NEUTRAL, "zeroGravity");


        addAttributeModifier(Attribute.GRAVITY, -0.05, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
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

