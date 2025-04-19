package capstone.team1.eventHorizon.events.utility;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import capstone.team1.eventHorizon.utility.MsgUtility;
import static org.bukkit.Bukkit.getServer;

/**
 * Utility class for handling player-related operations in the EventHorizon plugin.
 * Provides methods for managing and organizing players for various events.
 */
public class PlayerUtility {

    /**
     * Generates random pairs of online players for pair-based events.
     * Players are randomly shuffled before being paired together.
     * If there's an odd number of players, the last player will remain unpaired
     * and a warning message will be logged.
     *
     * @return A collection of player pairs, where each pair contains two randomly matched players.
     *         If there's an odd number of players, the last player will not be included in any pair.
     */
    public static Collection<Pair<Player, Player>> generateRandomPlayerPairs() {
        // Get a list of all online players and shuffle it
        List<Player> onlinePlayers = new ArrayList<>(getServer().getOnlinePlayers());
        Collections.shuffle(onlinePlayers);

        Collection<Pair<Player, Player>> playerPairs = new ArrayList<>();

        ///  TODO: Should I use a while loop with iterators instead?
        // Loop through the shuffled list and pair players
        for (int i = 0; i < onlinePlayers.size(); i += 2) {
            if (i + 1 < onlinePlayers.size()) {
                Player player1 = onlinePlayers.get(i);
                Player player2 = onlinePlayers.get(i + 1);

                playerPairs.add(Pair.of(player1, player2));
                MsgUtility.warning(player1.getName() + " is paired with " + player2.getName());
            }
        }

        if (onlinePlayers.size() % 2 != 0) {
            Player unpairedPlayer = onlinePlayers.getLast();
            MsgUtility.warning("Unpaired player: " + unpairedPlayer.getName());
        }

        return playerPairs;
    }
}