package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.utility.fawe.BlockEditor;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericRegion;
import capstone.team1.eventHorizon.utility.MsgUtility;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseBlockModification extends BaseEvent
{
    protected GenericRegion region;
    protected  Material replacementBlock;
    protected  boolean isMaskInverted;
    protected Collection<BlockType> blockTypesToReplace;


    public BaseBlockModification(EventClassification classification, String eventName, GenericRegion region, Material replacementBlock, Collection<BlockType> blockTypesToReplace, boolean isMaskInverted) {
        super(classification, eventName);
        this.region = region;
        this.replacementBlock = replacementBlock;
        this.blockTypesToReplace = blockTypesToReplace;
        this.isMaskInverted = isMaskInverted;
    }

    public void execute(){
        applyBlockEditToAllPlayers(replacementBlock, blockTypesToReplace, isMaskInverted);
    }

    public void terminate(){
        BlockEditor.undoAllBlockModifications();
    }

    public void applyBlockEditToAllPlayers(Material replacementBlock, Collection<BlockType> blockTypesToReplace, boolean isMaskInverted) {
        int successCount = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            try {
                BlockEditor.replaceBlocksInRegion(this.region.getRegion(player), replacementBlock, blockTypesToReplace, isMaskInverted);
                successCount++;
            } catch (Exception e) {
                MsgUtility.warning("Failed to apply block edit to player " + player.getName() + ": " + e.getMessage());
            }
        }
        MsgUtility.log("<green>Applied block edit to " + successCount + "/" + players.size() + " players for event: " + this.eventName);
    }

}
