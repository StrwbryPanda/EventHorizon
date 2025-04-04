package capstone.team1.eventHorizon.events.blockModification;

import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.utility.faweUtil.BlockEditor;
import capstone.team1.eventHorizon.events.utility.faweUtil.RegionSelector;
import capstone.team1.eventHorizon.utility.MsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBlockModification extends BaseEvent
{
    protected String regionStyle;
    protected String blockId;
    protected  int radius;
    protected int height;

    public BaseBlockModification(EventClassification classification, String eventName, String regionStyle, int radius, int height, String blockId) {
        super(classification, eventName);
        this.regionStyle = regionStyle;
        this.radius = radius;
        this.height = height;
        this.blockId = blockId;
    }

    public BaseBlockModification(EventClassification classification, String eventName, String regionStyle, int radius, int height) {
        super(classification, eventName);
        this.regionStyle = regionStyle;
        this.radius = radius;
        this.height = height;
    }

    public BaseBlockModification(EventClassification classification, String eventName) {
        super(classification, eventName);
    }

    public void execute(){
        applyBlockEditToAllPlayers(regionStyle,radius, height, blockId);
    }

    public void terminate(){
        BlockEditor.undoAllBlockModifications();
    }

    public void applyBlockEditToAllPlayers(String regionShape, int radius, int height, String blockId) {
        int successCount = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            try {
                switch (regionShape){
                    case "cylinderAround":
                        BlockEditor.replaceBlocksInRegion(RegionSelector.selectCylindricalRegionAroundPlayer(player, radius, height), blockId);
                        break;
                    case "cuboidAround":
                        BlockEditor.replaceBlocksInRegion(RegionSelector.selectCuboidRegionAroundPlayer(player, radius, height), blockId);
                        break;
                    case "cylinderAtFeet":
                        BlockEditor.replaceBlocksInRegion(RegionSelector.selectCylindricalRegionAtPlayersFeet(player, radius, height), blockId);
                        break;
                    case "cuboidAtFeet":
                        BlockEditor.replaceBlocksInRegion(RegionSelector.selectCuboidRegionAtPlayersFeet(player, radius, height), blockId);
                        break;
                    default:
                        MsgUtil.warning("Invalid region shape: " + regionShape);
                        return;
                }
                successCount++;
            } catch (Exception e) {
                MsgUtil.warning("Failed to apply block edit to player " + player.getName() + ": " + e.getMessage());
            }
        }
        MsgUtil.log("<green>Applied block edit to " + successCount + "/" + players.size() + " players for event: " + this.eventName);
    }

}
