package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that gives the player a slowness effect for 6000 ticks (5 minutes)
 */
public class YoureTooSlow extends BaseEffects {
    public YoureTooSlow() {
        super(EventClassification.NEGATIVE, "youreTooSlow");
        addEffect(PotionEffectType.SLOWNESS, 6000, 1,
                false, false, true);
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
