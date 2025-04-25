package capstone.team1.eventHorizon.events.inventoryAdjustments;

import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.events.utility.PlayerUtility;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collection;

public class InventorySwap extends BaseInventoryAdjustment{
    private Collection<Pair<Player, Player>> playerPairs;
    private final Sound swapSound = Sound.ENTITY_ENDERMAN_TELEPORT;

    public InventorySwap(){
        super(EventClassification.NEUTRAL, "inventorySwap");
    }

    @Override
    protected boolean applyToPlayer(Player player) {
        return false;
    }

    @Override
    public int applyToAllPlayers() {
        int pairsAffected = 0;

        for (Pair<Player, Player> pair : playerPairs) {
            Player player1 = pair.getLeft();
            Player player2 = pair.getRight();

            // Skip players who are no longer online
            if (!player1.isOnline() || !player2.isOnline()) {
                MsgUtility.warning("Skipping pair because one or both players went offline: "
                        + player1.getName() + " and " + player2.getName());
                continue;
            }

            // Perform the swap
            boolean success = swapInventories(player1, player2);

            if (success) {
                pairsAffected++;

                // Notify players
                player1.sendMessage("§6Your inventory has been swapped with " + player2.getName() + "!");
                player2.sendMessage("§6Your inventory has been swapped with " + player1.getName() + "!");

                // Play swap sound
                if (swapSound != null) {
                    player1.playSound(player1.getLocation(), swapSound, 1.0f, 1.0f);
                    player2.playSound(player2.getLocation(), swapSound, 1.0f, 1.0f);
                }

                MsgUtility.log("Swapped inventories between " + player1.getName() + " and " + player2.getName());
            }
        }
        return pairsAffected;
    }

    private boolean swapInventories(Player player1, Player player2) {
        try {
            // Get player inventories
            PlayerInventory inv1 = player1.getInventory();
            PlayerInventory inv2 = player2.getInventory();

            // Create copies of all inventory contents
            ItemStack[] contents1 = inv1.getContents().clone();
            ItemStack[] contents2 = inv2.getContents().clone();

            // Clear both inventories first to avoid potential issues
            inv1.clear();
            inv2.clear();

            // Set each inventory with the other player's contents
            inv1.setContents(contents2);
            inv2.setContents(contents1);

            return true;
        } catch (Exception e) {
            MsgUtility.warning("Error swapping inventories between " + player1.getName() +
                    " and " + player2.getName() + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Executes the InventorySwap event.
     */
    @Override
    public void execute() {
        // Generate random player pairs
        playerPairs = PlayerUtility.generateRandomPlayerPairs();

        super.execute();
    }

    /**
     * Terminates the InventorySwap event.
     */
    @Override
    public void terminate() {
        super.terminate();
        playerPairs = null;
    }
}
