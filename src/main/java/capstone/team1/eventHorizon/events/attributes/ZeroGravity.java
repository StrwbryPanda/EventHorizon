package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ZeroGravity extends BaseAttribute {

    public ZeroGravity() {
        super(EventClassification.NEUTRAL, "zeroGravity");
        
        addAttributeModifier(Attribute.GRAVITY, -0.05, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    }
    
    public void applyZeroGravityToEntities() {
        for (Entity entity : EventHorizon.getPlugin().getServer().getWorlds().get(0).getEntities()) {
            if (!(entity instanceof Player)) {
                entity.setGravity(false);
            }
        }
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