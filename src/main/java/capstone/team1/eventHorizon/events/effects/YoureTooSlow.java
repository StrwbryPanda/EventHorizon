package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that gives players a movement-reducing slowness effect.
 * This event applies a level 2 slowness effect for 6000 ticks (5 minutes),
 * making players move significantly slower than normal.
 */
public class YoureTooSlow extends BaseEffects {
    /**
     * Constructs a new YoureTooSlow event with NEGATIVE classification.
     * Initializes the event with a moderate-level slowness effect.
     */
    public YoureTooSlow() {
        super(EventClassification.NEGATIVE, "youreTooSlow");
        addEffect(PotionEffectType.SLOWNESS, 6000, 1,
                false, false, true);
    }

    /**
     * Executes the YoureTooSlow event by applying the slowness effect to all players.
     * Delegates execution to the parent class.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the YoureTooSlow event by removing the slowness effect from affected players.
     * Delegates termination to the parent class.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
