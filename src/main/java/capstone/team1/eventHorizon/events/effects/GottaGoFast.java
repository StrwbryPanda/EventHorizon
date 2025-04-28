package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that gives players a significant speed boost effect.
 * This event applies a level 3 speed effect for 6000 ticks (5 minutes),
 * making players move considerably faster than normal.
 */
public class GottaGoFast extends BaseEffects {
    /**
     * Constructs a new GottaGoFast event with POSITIVE classification.
     * Initializes the event with a high-level speed effect.
     */
    public GottaGoFast() {
        super(EventClassification.POSITIVE, "gottaGoFast");
        addEffect(PotionEffectType.SPEED, 6000, 2,
                false, false, true);
    }

    /**
     * Executes the GottaGoFast event by applying the speed effect to all players.
     * Delegates execution to the parent class.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the GottaGoFast event by removing the speed effect from affected players.
     * Delegates termination to the parent class.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
