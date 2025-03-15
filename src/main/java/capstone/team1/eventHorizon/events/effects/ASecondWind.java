package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

//Player strength (attack power/damage dealt) is doubled
public class ASecondWind extends BaseEffects {
    public ASecondWind() {
        super(EventClassification.POSITIVE, "aSecondWind");
        addEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1,
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
