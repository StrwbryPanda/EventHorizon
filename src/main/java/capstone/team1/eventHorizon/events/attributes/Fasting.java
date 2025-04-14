package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Player;

public class Fasting extends BaseAttribute {

    public Fasting() {
        super(EventClassification.NEGATIVE, "fasting");
    }

    @Override
    public void applyAttributeModifiersToPlayer(Player player) {
        super.applyAttributeModifiersToPlayer(player);
        player.setFoodLevel(0);
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

