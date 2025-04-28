package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.blockModification.subEvents.subSpawnIceMobs;
import capstone.team1.eventHorizon.events.blockModification.subEvents.subWaterToLava;
import capstone.team1.eventHorizon.events.utility.fawe.BlockEditor;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCuboidRegion;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCylindricalRegion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;

/**
 * A neutral event that creates an icy environment by replacing ground blocks with packed ice
 * in a cylindrical region and spawns ice-themed mobs.
 * The event transforms the terrain into a winter wonderland with a radius of 100 blocks
 * and a height of 10 blocks.
 */
public class IceIsNice extends BaseBlockModification
{
    /**
     * Constructs a new IceIsNice event.
     * Creates a cylindrical region with radius 100 and height 10 where ground blocks
     * will be replaced with packed ice.
     */
    public IceIsNice()
    {
        super(EventClassification.NEUTRAL, "iceIsNice", new GenericCylindricalRegion(100,10,0), Material.PACKED_ICE, EventHorizon.getBlockMasks().getGroundBlocks(), false);
    }

    /**
     * Executes the ice transformation event and spawns ice-themed mobs.
     * First replaces blocks with packed ice, then schedules the spawning of ice mobs
     * on the next server tick.
     */
    public void execute(){
        super.execute(false);
        Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> new subSpawnIceMobs().execute());

    }

    /**
     * Terminates the ice event by clearing all active edit sessions.
     * This method overrides the parent class terminate method to use a different cleanup approach.
     */
    @Override
    public void terminate(){
        BlockEditor.clearActiveEditSessions();
    }
}
