package capstone.team1.eventHorizon.events.utility;

import capstone.team1.eventHorizon.EventHorizon;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

/**
 * Listener class responsible for managing entities when they are added to the world.
 * This class handles the removal of marked entities as they enter the world,
 * ensuring that entities marked for deletion are removed immediately.
 *
 * Implements Bukkit's Listener interface to handle specific events
 * related to entity world entry management.
 */
public class EntityAddToWorldListener implements Listener {

    /**
     * Handles the EntityAddToWorldEvent by removing marked entities when they enter the world.
     * This method is called automatically when an entity is added to the world.
     *
     * @param event The EntityAddToWorldEvent containing information about the entity being added
     */
    @EventHandler
    public void onEntityAddedToWorld(EntityAddToWorldEvent event) {
        Entity entity = event.getEntity();
        EventHorizon.entityKeysToDelete.forEach(key -> {
            if (isEntityMarked(entity, key)) {
                entity.remove();
            }
        });
    }

    /**
     * Checks if an entity is marked with a specific NamespacedKey.
     * Used to identify entities that should be removed from the world.
     *
     * @param entity The Entity to check for marking
     * @param key The NamespacedKey to look for in the entity's persistent data
     * @return true if the entity is marked with the specified key, false otherwise
     */
    public boolean isEntityMarked(Entity entity, NamespacedKey key) {
        return entity.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}
