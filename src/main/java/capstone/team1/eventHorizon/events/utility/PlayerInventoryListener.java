package capstone.team1.eventHorizon.events.utility;

import capstone.team1.eventHorizon.EventHorizon;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

/**
 * Listener class responsible for managing player inventory events.
 * This class handles the removal of marked items from player inventories,
 * particularly when players join the server.
 *
 * Implements Bukkit's Listener interface to handle specific events
 * related to player inventory management.
 */
public class PlayerInventoryListener implements Listener {
    /**
     * Handles the PlayerJoinEvent by removing marked items from the joining player's inventory.
     * This method is called automatically when a player joins the server.
     *
     * @param event The PlayerJoinEvent containing information about the joining player
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        removeMarkedItems(player);
    }

    /**
     * Removes items marked with specific keys from a player's inventory.
     * Iterates through the inventory and checks each item for marked keys that should be deleted.
     *
     * @param player The player whose inventory will be checked for marked items
     */
    public static void removeMarkedItems(Player player) {
        PlayerInventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item == null) continue;

            final int slot = i;

            EventHorizon.entityKeysToDelete.forEach(key -> {
                if (isItemMarked(item, key)) {
                    inventory.setItem(slot, null);
                }
            });
        }
    }

    /**
     * Checks if an item is marked with a specific NamespacedKey.
     * Used to identify items that should be removed from player inventories.
     *
     * @param item The ItemStack to check for marking
     * @param key The NamespacedKey to look for in the item's persistent data
     * @return true if the item is marked with the specified key, false otherwise
     */
    public static boolean isItemMarked(ItemStack item, NamespacedKey key) {
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}
