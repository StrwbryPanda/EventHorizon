package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.blockModification.subEvents.subGrassToFire;
import capstone.team1.eventHorizon.events.blockModification.subEvents.subWaterToLava;
import capstone.team1.eventHorizon.events.mobSpawn.NetherRaid;
import capstone.team1.eventHorizon.events.utility.fawe.BlockEditor;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCylindricalRegion;
import org.bukkit.Bukkit;

/**
 * A negative event that transforms the environment into a Nether-like landscape.
 * Creates a cylindrical region of 100 blocks radius and 400 blocks height where ground blocks
 * are replaced with Nether-themed blocks, water is converted to lava, grass is set on fire,
 * and Nether mobs are spawned.
 */
public class NetherInvasion extends BaseBlockModification
{
    /**
     * Constructs a new NetherInvasion event.
     * Creates a cylindrical region with radius 100 and height 400 where ground blocks
     * will be replaced with a random Nether-themed pattern.
     */
    public NetherInvasion()
    {
        super(EventClassification.NEGATIVE, "netherInvasion", new GenericCylindricalRegion(100,400,200),
                EventHorizon.getRandomPatterns().getNetherPattern(), EventHorizon.getBlockMasks().getGroundBlocks(), false);

    }

    /**
     * Executes the Nether invasion event by transforming blocks and spawning mobs.
     * First applies the Nether pattern transformation, then schedules water-to-lava conversion,
     * grass-to-fire conversion, and spawns Nether mobs on the next server tick.
     */
    public void execute(){
        super.execute(true);
        Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> new subWaterToLava().execute(false));
        Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> new subGrassToFire().execute(false));
        Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> new NetherRaid().execute());
    }

    /**
     * Terminates the Nether invasion event by clearing all active edit sessions.
     * This method overrides the parent class terminate method to use a different cleanup approach.
     */
    @Override
    public void terminate(){
        BlockEditor.clearActiveEditSessions();

    }
}
