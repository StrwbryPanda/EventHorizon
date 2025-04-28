package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCuboidRegion;
import org.bukkit.Material;

/**
 * A positive event that replaces underground blocks with gold ore in a cuboid region.
 * This event creates a "gold rush" scenario by transforming regular blocks into gold ore deposits.
 */
public class GoldRush extends BaseBlockModification
{
    /**
     * Constructs a new GoldRush event.
     * Creates a cuboid region of 50x400x200 blocks where underground blocks
     * will be replaced with gold ore.
     */
    public GoldRush()
    {
        super(EventClassification.POSITIVE, "goldRush", new GenericCuboidRegion(50,400,200), Material.GOLD_ORE, EventHorizon.getBlockMasks().getUndergroundBlocks(), false);
    }

    /**
     * Executes the gold rush event using single block replacement mode.
     * Delegates to parent class execute method.
     */
    public void execute(){
        super.execute(false);
    }

    /**
     * Terminates the gold rush event and undoes all block modifications.
     * Delegates to parent class terminate method.
     */
    @Override
    public void terminate(){
        super.terminate();
    }
}
