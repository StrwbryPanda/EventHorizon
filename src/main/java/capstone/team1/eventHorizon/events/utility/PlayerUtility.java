package capstone.team1.eventHorizon.events.utility;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import capstone.team1.eventHorizon.utility.MsgUtil;
import static org.bukkit.Bukkit.getServer;

public class PlayerUtility {
    // Generates random player pairs from the online players
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
                MsgUtil.warning(player1.getName() + " is paired with " + player2.getName());
            }
        }

        if (onlinePlayers.size() % 2 != 0) {
            Player unpairedPlayer = onlinePlayers.getLast();
            MsgUtil.warning("Unpaired player: " + unpairedPlayer.getName());
        }

        return playerPairs;
    }
}
