package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class HalfAHeart extends BaseAttribute {
    public HalfAHeart() {
        super(EventClassification.NEGATIVE, "halfAHeart");
        addAttributeModifier(Attribute.MAX_HEALTH, -19.0, AttributeModifier.Operation.ADD_NUMBER);
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
