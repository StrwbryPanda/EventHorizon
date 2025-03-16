package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that gives the player saturation and slowness effects for 6000 ticks (5 minutes)
 */
public class FoodComa extends BaseEffects {
    public FoodComa() {
        super(EventClassification.POSITIVE, "foodComa");
        addEffect(PotionEffectType.SATURATION, 6000, 1,
                false, true, true);
        addEffect(PotionEffectType.SLOWNESS, 6000, 1,
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
