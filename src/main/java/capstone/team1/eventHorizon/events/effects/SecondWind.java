package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that gives the player a strength effect for 6000 ticks (5 minutes)
 */
public class SecondWind extends BaseEffects {
    public SecondWind() {
        super(EventClassification.POSITIVE, "SecondWind");
        addEffect(PotionEffectType.STRENGTH, 6000, 1,
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
