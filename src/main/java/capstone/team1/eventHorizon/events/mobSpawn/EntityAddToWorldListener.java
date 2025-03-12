package capstone.team1.eventHorizon.events.mobSpawn;

import capstone.team1.eventHorizon.EventHorizon;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class EntityAddToWorldListener implements Listener {
    @EventHandler
    public void onEntityAddedToWorld(EntityAddToWorldEvent event) {
        Entity entity = event.getEntity();
        EventHorizon.entityKeysToDelete.forEach(key -> {
            if (isEntityMarked(entity, key)) {
                entity.remove();
            }
        });
    }

    public boolean isEntityMarked(Entity entity, NamespacedKey key) {
        return entity.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}
