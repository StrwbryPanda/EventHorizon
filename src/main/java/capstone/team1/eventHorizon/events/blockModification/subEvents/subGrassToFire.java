package capstone.team1.eventHorizon.events.blockModification.subEvents;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.blockModification.BaseBlockModification;
import capstone.team1.eventHorizon.events.utility.fawe.BlockEditor;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCylindricalRegion;
import org.bukkit.Material;

/**
 * A block modification event that replaces grass and plant blocks with fire within a cylindrical region.
 * This event is classified as negative due to its destructive nature of converting vegetation to fire.
 * Extends BaseBlockModification to utilize block replacement functionality.
 */
public class subGrassToFire extends BaseBlockModification
{
    /**
     * Constructs a new subGrassToFire event.
     * Initializes the event with:
     * - Negative classification
     * - Event name "subGrassToFire"
     * - Cylindrical region with radius 100 and height 400, centered at y=200
     * - Replacement material set to FIRE
     * - Block type mask targeting only plant blocks
     * - Non-inverted mask behavior
     */
    public subGrassToFire()
    {
        super(EventClassification.NEGATIVE, "subGrassToFire", new GenericCylindricalRegion(100,400,200), Material.FIRE, EventHorizon.getBlockMasks().getPlants(), false);
    }

    /**
     * Executes the grass to fire conversion event.
     * Uses pattern-based replacement mode.
     */
    public void execute(){
        super.execute(true);
    }

    /**
     * Terminates the event and cleans up any active edit sessions.
     * This ensures that no lingering block modifications remain active.
     */
    @Override
    public void terminate(){
        BlockEditor.clearActiveEditSessions();
    }
}
