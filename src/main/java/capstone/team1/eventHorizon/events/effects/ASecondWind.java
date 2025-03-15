package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

//Player strength (attack power/damage dealt) is doubled
public class ASecondWind extends BaseEffects {
    public ASecondWind() {
        super(PotionEffectType.STRENGTH, EventClassification.POSITIVE, "aSecondWind");
        setDuration(Integer.MAX_VALUE)
                .setAmplifier(0)
                .setAmbient(false)
                .setShowParticles(true)
                .setShowIcon(true);
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
    }

    public void terminate(){};

}
