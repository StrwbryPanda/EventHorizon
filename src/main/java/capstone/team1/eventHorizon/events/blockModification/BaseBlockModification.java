package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.utility.fawe.BlockEditor;
import capstone.team1.eventHorizon.events.utility.fawe.region.GenericRegion;
import capstone.team1.eventHorizon.utility.MsgUtility;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base class for block modification events that handles replacing blocks in specified regions.
 * Provides functionality to modify blocks for all online players using either single blocks or patterns.
 */
public abstract class BaseBlockModification extends BaseEvent
{
    /** The region in which blocks will be modified */
    protected GenericRegion region;
    /** The material to replace blocks with when not using patterns */
    protected  Material replacementBlock;
    /** The pattern to replace blocks with when using patterns */
    protected  Pattern replacingPattern;
    /** Whether the block type mask should be inverted */
    protected  boolean isMaskInverted;
    /** Collection of block types to be replaced */
    protected Collection<BlockType> blockTypesToReplace;

    /**
     * Constructs a block modification event using a single replacement block.
     *
     * @param classification The event classification (POSITIVE, NEGATIVE, or NEUTRAL)
     * @param eventName The name of the event
     * @param region The region in which blocks will be modified
     * @param replacementBlock The material to replace blocks with
     * @param blockTypesToReplace Collection of block types to be replaced
     * @param isMaskInverted Whether to invert the block type mask
     */
    public BaseBlockModification(EventClassification classification, String eventName, GenericRegion region, Material replacementBlock, Collection<BlockType> blockTypesToReplace, boolean isMaskInverted) {
        super(classification, eventName);
        this.region = region;
        this.replacementBlock = replacementBlock;
        this.blockTypesToReplace = blockTypesToReplace;
        this.isMaskInverted = isMaskInverted;
    }

    /**
     * Constructs a block modification event using a pattern.
     *
     * @param classification The event classification (POSITIVE, NEGATIVE, or NEUTRAL)
     * @param eventName The name of the event
     * @param region The region in which blocks will be modified
     * @param pattern The pattern to replace blocks with
     * @param blockTypesToReplace Collection of block types to be replaced
     * @param isMaskInverted Whether to invert the block type mask
     */
    public BaseBlockModification(EventClassification classification, String eventName, GenericRegion region, Pattern pattern, Collection<BlockType> blockTypesToReplace, boolean isMaskInverted) {
        super(classification, eventName);
        this.region = region;
        this.replacingPattern = pattern;
        this.blockTypesToReplace = blockTypesToReplace;
        this.isMaskInverted = isMaskInverted;
    }

    /**
     * Executes the block modification event for all players.
     *
     * @param isUsingPattern Whether to use pattern-based replacement instead of single block replacement
     */
    public void execute(boolean isUsingPattern){
        if(!isUsingPattern) {
            applyBlockEditToAllPlayers(replacementBlock, blockTypesToReplace, isMaskInverted);
            return;
        }
        applyBlockEditToAllPlayers(replacingPattern, blockTypesToReplace, isMaskInverted);
    }

    /**
     * Terminates the block modification event and undoes all modifications.
     */
    public void terminate(){
        BlockEditor.undoAllBlockModifications();
    }

    /**
     * Applies block modifications to all online players using a single replacement block.
     *
     * @param replacementBlock The material to replace blocks with
     * @param blockTypesToReplace Collection of block types to be replaced
     * @param isMaskInverted Whether to invert the block type mask
     */
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

    /**
     * Applies block modifications to all online players using a pattern.
     *
     * @param replacingPattern The pattern to replace blocks with
     * @param blockTypesToReplace Collection of block types to be replaced
     * @param isMaskInverted Whether to invert the block type mask
     */
    public void applyBlockEditToAllPlayers(Pattern replacingPattern, Collection<BlockType> blockTypesToReplace, boolean isMaskInverted) {
        int successCount = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            try {
                BlockEditor.replaceBlocksInRegion(this.region.getRegion(player), replacingPattern, blockTypesToReplace, isMaskInverted);
                successCount++;
            } catch (Exception e) {
                MsgUtility.warning("Failed to apply block edit to player " + player.getName() + ": " + e.getMessage());
            }
        }
        MsgUtility.log("<green>Applied block edit to " + successCount + "/" + players.size() + " players for event: " + this.eventName);
    }
}
