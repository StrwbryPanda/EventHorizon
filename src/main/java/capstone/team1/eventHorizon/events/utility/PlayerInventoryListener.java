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

public class PlayerInventoryListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        removeMarkedItems(player);
    }

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

    public static boolean isItemMarked(ItemStack item, NamespacedKey key) {
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}
