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
 * Event that equips the player with a Curse of Binding Elytra and adds fireworks to their inventory
 */
public class FlightSchool extends BaseInventoryAdjustment {

    public FlightSchool() {
        super(EventClassification.NEUTRAL, "flightSchool");
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

    @Override
    protected boolean applyToPlayer(Player player) {
        // Equip the Elytra with Curse of Binding
        boolean applied = equipPlayerItem(player, EquipmentSlot.CHEST, equipmentItems.get(EquipmentSlot.CHEST).clone());

        // Add fireworks to player's inventory
        addPlayerInvItems(player, java.util.List.of(itemType));

        return applied;
    }

    @Override
    protected void onOperationPerformed(Player player) {
        // Add stuff here when the event is applied
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void terminate() {
        super.terminate();
        // When the event ends, we don't want to remove the cursed elytras (that's part of the chaos)
        // but we could clean up any extra fireworks we might have dropped
        deleteMarkedItems();
    }
}