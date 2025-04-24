package capstone.team1.eventHorizon.events.inventoryAdjustments;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Event that replaces the player's food with rotten flesh.
 * This negative event transforms all edible items in players' inventories into rotten flesh.
 */
public class SpoiledFood extends BaseInventoryAdjustment {
    /**
     * Constructs a new SpoiledFood event.
     * Initializes the event as NEGATIVE classification and configures:
     * - Continuous operation disabled
     * - Rotten flesh as default item type for replacements
     */
    public SpoiledFood() {
        super(EventClassification.NEGATIVE, "spoiledFood");
        // Configure the event parameters
        this.useContinuousOperation = false;

        // Set the default item type to rotten flesh
        this.itemType = new ItemStack(Material.ROTTEN_FLESH);
    }

    /**
     * Applies the spoiled food effect to a specific player.
     * Replaces all edible items in the player's inventory with rotten flesh.
     *
     * @param player the target player
     * @return true if any food items were replaced
     */
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

    /**
     * Executes the SpoiledFood event.
     * Calls parent class execution logic.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the SpoiledFood event.
     * Calls parent class termination logic.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
