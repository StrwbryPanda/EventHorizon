package capstone.team1.eventHorizon.events.effects;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.potion.PotionEffectType;

/**
 * Event that gives the player saturation and slowness effects for 6000 ticks (5 minutes)
 */
public class FoodComa extends BaseEffects
{
    /**
     * Constructs a new FoodComa event with NEUTRAL classification.
     * Initializes the event with saturation and slowness effects.
     */
    public FoodComa()
    {
        super(EventClassification.NEUTRAL, "foodComa");
        addEffect(PotionEffectType.SATURATION, 6000, 0,
                false, false, true);
        addEffect(PotionEffectType.SLOWNESS, 6000, 0,
                false, false, true);
    }

    /**
     * Executes the FoodComa event by applying saturation and slowness effects to all players.
     * Delegates execution to the parent class.
     */
    @Override
    public void execute()
    {
        super.execute();
    }

    /**
     * Terminates the FoodComa event by removing saturation and slowness effects from affected players.
     * Delegates termination to the parent class.
     */
    @Override
    public void terminate()
    {
        super.terminate();
    }
}
