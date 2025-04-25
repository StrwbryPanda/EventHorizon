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

public class IceIsNice extends BaseBlockModification
{
    public IceIsNice()
    {
        super(EventClassification.NEUTRAL, "iceIsNice", new GenericCylindricalRegion(100,10,0), Material.PACKED_ICE, EventHorizon.getBlockMasks().getGroundBlocks(), false);
    }
    public void execute(){
        super.execute(false);
        Bukkit.getScheduler().runTask(EventHorizon.getPlugin(), task -> new subSpawnIceMobs().execute());

    }
    @Override
    public void terminate(){
        BlockEditor.clearActiveEditSessions();
    }
}
