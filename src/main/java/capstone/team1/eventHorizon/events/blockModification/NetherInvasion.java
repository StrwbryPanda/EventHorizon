package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.blockModification.subEvents.subGrassToFire;
import capstone.team1.eventHorizon.events.blockModification.subEvents.subWaterToLava;
import capstone.team1.eventHorizon.events.mobSpawn.NetherRaid;
import capstone.team1.eventHorizon.events.utility.fawe.BlockEditor;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericCylindricalRegion;
import org.bukkit.Bukkit;

public class NetherInvasion extends BaseBlockModification
{
    public NetherInvasion()
    {
        super(EventClassification.NEGATIVE, "netherInvasion", new GenericCylindricalRegion(100,400,200),
                EventHorizon.getRandomPatterns().getNetherPattern(), EventHorizon.getBlockMasks().getGroundBlocks(), false);

    }
    public void execute(){
        super.execute(true);
        Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> new subWaterToLava().execute(false));
        Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> new subGrassToFire().execute(false));
        Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> new NetherRaid().execute());

    }
    @Override
    public void terminate(){
        BlockEditor.clearActiveEditSessions();

    }
}
