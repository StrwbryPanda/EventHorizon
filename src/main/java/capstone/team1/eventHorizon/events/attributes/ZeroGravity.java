package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Represents an event that simulates zero gravity conditions in the game.
 * This event modifies player gravity and movement speed attributes, and can affect other entities in the world.
 * Extends {@link BaseAttribute} and is classified as a NEUTRAL event.
 */
public class ZeroGravity extends BaseAttribute {

    /**
     * Constructs a new ZeroGravity event.
     * Initializes the event with NEUTRAL classification and applies gravity and movement speed modifiers.
     */
    public ZeroGravity() {
        super(EventClassification.NEUTRAL, "zeroGravity");
        
        addAttributeModifier(Attribute.GRAVITY, -0.05, AttributeModifier.Operation.ADD_NUMBER);
        addAttributeModifier(Attribute.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    }

    /**
     * Applies zero gravity effect to all non-player entities in the main world.
     * Players are excluded from this effect as they are handled through attributes.
     */
    public void applyZeroGravityToEntities() {
        for (Entity entity : EventHorizon.getPlugin().getServer().getWorlds().get(0).getEntities()) {
            if (!(entity instanceof Player)) {
                entity.setGravity(false);
            }
        }
    }

    /**
     * Executes the zero gravity event by calling the parent's execute method.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the zero gravity event by calling the parent's terminate method.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}