package capstone.team1.eventHorizon.events.inventoryAdjustments;

import capstone.team1.eventHorizon.events.EventClassification;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Event that equips the player with a Curse of Binding Elytra and adds fireworks to their inventory.
 * This positive event provides players with flight capabilities and resources.
 */
public class FlightSchool extends BaseInventoryAdjustment {

    /**
     * Constructs a new FlightSchool event.
     * Initializes the event as POSITIVE classification and configures:
     * - Continuous operation disabled
     * - Stack of 64 firework rockets as default item
     * - Unbreakable Elytra with Curse of Binding named "Training Wings"
     */
    public FlightSchool() {
        super(EventClassification.POSITIVE, "flightSchool");
        // Configure the event parameters
        this.useContinuousOperation = false;

        // Set the fireworks as the default item type for adding to inventory
        this.itemType = new ItemStack(Material.FIREWORK_ROCKET, 64);

        // Create Elytra with Curse of Binding
        ItemStack bindingElytra = new ItemStack(Material.ELYTRA);
        ItemMeta elytraMeta = bindingElytra.getItemMeta();
        elytraMeta.customName(Component.text("Training Wings").color(NamedTextColor.GOLD));
        elytraMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        elytraMeta.setUnbreakable(true);
        bindingElytra.setItemMeta(elytraMeta);

        // Set up equipment items - specifically the elytra for the chest slot
        equipmentItems.put(EquipmentSlot.CHEST, bindingElytra);
    }

    /**
     * Applies the flight school effect to a specific player.
     * Equips the cursed Elytra and adds firework rockets to inventory.
     *
     * @param player the target player
     * @return true if Elytra was equipped successfully
     */
    @Override
    protected boolean applyToPlayer(Player player) {
        // Equip the Elytra with Curse of Binding
        boolean applied = equipPlayerItem(player, EquipmentSlot.CHEST, equipmentItems.get(EquipmentSlot.CHEST).clone());

        // Add fireworks to player's inventory
        addPlayerInvItems(player, java.util.List.of(itemType));

        return applied;
    }

    /**
     * Hook method called after successful operation on a player.
     * Currently empty, available for future implementation.
     *
     * @param player affected player
     */
    @Override
    protected void onOperationPerformed(Player player) {
        // Add stuff here when the event is applied
    }

    /**
     * Executes the FlightSchool event.
     * Calls parent class execution logic.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Terminates the FlightSchool event.
     * Cleans up by deleting all marked items.
     */
    @Override
    public void terminate() {
        super.terminate();

        // Delete all marked items
        deleteMarkedItems();
    }
}