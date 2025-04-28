package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that gives players an enhanced strength effect.
 * This event applies a level 2 strength effect for 6000 ticks (5 minutes),
 * allowing players to deal more damage than normal.
 */
public class SecondWind extends BaseEffects {
    /**
     * Constructs a new SecondWind event with POSITIVE classification.
     * Initializes the event with a moderate-level strength effect.
     */
    public SecondWind() {
        super(EventClassification.POSITIVE, "SecondWind");
        addEffect(PotionEffectType.STRENGTH, 6000, 1,
                false, false, true);
    }

    /**
     * Executes the SecondWind event by applying the strength effect to all players.
     * Delegates execution to the parent class.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the SecondWind event by removing the strength effect from affected players.
     * Delegates termination to the parent class.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
