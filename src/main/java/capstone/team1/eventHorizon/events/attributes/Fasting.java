package capstone.team1.eventHorizon.events.attributes;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.entity.Player;

public class Fasting extends AttributesBase {
    public Fasting() {
        super(EventClassification.NEGATIVE, "fasting");
    }
    @Override
    public void applyEffect(Player player) {
        player.setFoodLevel(0);
        player.sendMessage("Your hunger has been completely depleted!");
    }
    @Override
    public void execute() {
        super.execute();
    }
    public void terminate(){};

}
