package capstone.team1.eventHorizon.events.inventoryAdjustments;

import capstone.team1.eventHorizon.events.EventClassification;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Event that causes the player to randomly drop the item in their main hand at random intervals
 */
public class ButterFingers extends BaseInventoryAdjustment {
    public ButterFingers() {
        super(EventClassification.NEGATIVE, "butterFingers");
        // Configure the event parameters
        this.useContinuousOperation = true;
        this.setOperationInterval(ThreadLocalRandom.current().nextInt(5, 61));
    }

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

    @Override
    public void execute() {
        // Set a random interval before starting continuous operation
        this.setOperationInterval(ThreadLocalRandom.current().nextInt(5, 61));
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
    }
}
