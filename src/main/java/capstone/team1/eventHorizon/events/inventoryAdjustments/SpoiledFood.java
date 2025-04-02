package capstone.team1.eventHorizon.events.inventoryAdjustments;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Event that replaces the player's food with rotten flesh
 */
public class SpoiledFood extends BaseInventoryAdjustment {
    public SpoiledFood() {
        super(EventClassification.NEGATIVE, "spoiledFood");
        // Configure the event parameters
        this.useContinuousOperation = false;

        // Set the default item type to rotten flesh
        this.itemType = new ItemStack(Material.ROTTEN_FLESH);
    }

    @Override
    protected boolean applyToPlayer(Player player) {
        PlayerInventory inventory = player.getInventory();
        boolean applied = false;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType().isEdible() && item.getType() != Material.ROTTEN_FLESH) {
                // Make a temporary ItemStack for the replacement operation
                ItemStack spoiledFood = itemType.clone();
                spoiledFood.setAmount(item.getAmount());

                // Use replace method to replace the food with rotten flesh
                applied = replacePlayerInvItems(player, item, spoiledFood);
            }
        }


        return applied;
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
    }
}
