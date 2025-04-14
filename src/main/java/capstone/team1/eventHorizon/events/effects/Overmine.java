package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that gives the player a haste effect for 6000 ticks (5 minutes)
 */
public class Overmine extends BaseEffects {
    public Overmine() {
        super(EventClassification.POSITIVE, "overmine");
        addEffect(PotionEffectType.HASTE, 6000, 1,
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
