package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that gives the player a speed effect for 6000 ticks (5 minutes)
 */
public class GottaGoFast extends BaseEffects {
    public GottaGoFast() {
        super(EventClassification.POSITIVE, "gottaGoFast");
        addEffect(PotionEffectType.SPEED, 6000, 1,
                false, true, true);
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
