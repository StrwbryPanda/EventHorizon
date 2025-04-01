package capstone.team1.eventHorizon.events.inventoryAdjustments;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Player;

/**
 * Event that replaces the player's food with rotten flesh
 */
public class SpoiledFood extends BaseInventoryAdjustment {
    public SpoiledFood() {
        super(EventClassification.NEGATIVE, "spoiledFood");

    }

    @Override
    protected boolean applyToPlayer(Player player) {
        boolean applied = false;



        return applied;
    }
}
