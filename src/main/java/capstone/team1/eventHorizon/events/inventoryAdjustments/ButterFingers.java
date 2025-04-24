package capstone.team1.eventHorizon.events.inventoryAdjustments;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Event that causes players to randomly drop items from their hands at random intervals.
 * This negative event continuously checks players' hands and forces them to drop held items.
 */
public class ButterFingers extends BaseInventoryAdjustment {
    /**
     * Constructs a new ButterFingers event.
     * Initializes the event as NEGATIVE classification with continuous operation enabled.
     * Sets a random initial operation interval between 5 and 60 seconds.
     */
    public ButterFingers() {
        super(EventClassification.NEGATIVE, "butterFingers");
        // Configure the event parameters
        this.useContinuousOperation = true;
        this.setOperationInterval(ThreadLocalRandom.current().nextInt(5, 61));
    }

    /**
     * Applies the butter fingers effect to a specific player.
     * Checks both main hand and off hand for items and forces the player to drop them.
     *
     * @param player the target player
     * @return true if an item was dropped, false if both hands were empty
     */
    @Override
    protected boolean applyToPlayer(Player player) {
        ItemStack handItem;

        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            handItem = player.getInventory().getItemInMainHand();
            player.getInventory().setItemInMainHand(null);
            player.dropItem(handItem);
        }
        else if (player.getInventory().getItemInOffHand().getType() != Material.AIR) {
            handItem = player.getInventory().getItemInOffHand();
            player.getInventory().setItemInOffHand(null);
            player.dropItem(handItem);
        }
        else {
            return false;
        }

        // Randomize the interval for the next operation
        this.setOperationInterval(ThreadLocalRandom.current().nextInt(5, 61));

        return true;
    }

    /**
     * Executes the ButterFingers event.
     * Sets an initial random interval and starts the continuous operation.
     */
    @Override
    public void execute() {
        // Set a random interval before starting continuous operation
        this.setOperationInterval(ThreadLocalRandom.current().nextInt(5, 61));
        super.execute();
    }

    /**
     * Terminates the ButterFingers event.
     * Stops the continuous operation task.
     */
    @Override
    public void terminate() {
        super.terminate();
    }
}
