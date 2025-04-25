package capstone.team1.eventHorizon.events.utility;

import capstone.team1.eventHorizon.EventHorizon;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Listener class responsible for tracking player-dropped items.
 * Ensures that dropped items maintain their marked status.
 */
public class PlayerDropItemListener implements Listener {
    /**
     * Handles the PlayerDropItemEvent by checking if the original item was marked
     * and applying the same mark to the dropped item entity.
     *
     * @param event The PlayerDropItemEvent containing the dropped item information
     */
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item droppedItem = event.getItemDrop();
        ItemStack itemStack = droppedItem.getItemStack();

        // Skip if the item has no item meta
        if (!itemStack.hasItemMeta()) return;

        // Get the persistent data container from the ItemStack's meta
        PersistentDataContainer itemPDC = itemStack.getItemMeta().getPersistentDataContainer();

        // Iterate through the entity keys to check if they are marked for deletion
        for (NamespacedKey key : EventHorizon.entityKeysToDelete) {
            if (itemPDC.has(key, PersistentDataType.BYTE)) {
                // Mark the dropped item entity
                droppedItem.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            }
        }
    }
}
