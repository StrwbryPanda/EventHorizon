package capstone.team1.eventHorizon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final JavaPlugin plugin;

    public ScoreboardManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(Player player) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("test", Criteria.DUMMY, ChatColor.GOLD + "Event Horizon");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                scoreboard.getEntries().forEach(scoreboard::resetScores); // clear the previous scoreboard

                String playerName = player.getName(); //get online player names

                objective.getScore(ChatColor.WHITE + playerName).setScore(player.getStatistic(Statistic.DEATHS)); //shows players' name and death count

                player.setScoreboard(scoreboard);
            }
        }.runTaskTimer(plugin, 0L, 20L); // repeats every second
    }

}
