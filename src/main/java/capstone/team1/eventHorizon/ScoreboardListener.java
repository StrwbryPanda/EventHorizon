package capstone.team1.eventHorizon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

public class ScoreboardListener implements Listener {

    private final ScoreboardManager ScoreboardManager;

    public ScoreboardListener(EventHorizon plugin, ScoreboardManager scoreboardManager) {
        this.ScoreboardManager = scoreboardManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ScoreboardManager.createScoreboard(player);
    }
}
