package capstone.team1.eventHorizon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final JavaPlugin plugin;

    public ScoreboardManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

//    public void createScoreboard(Player player) {
//        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
//        Scoreboard scoreboard = manager.getNewScoreboard();
//        Objective objective = scoreboard.registerNewObjective("test", Criteria.DUMMY, ChatColor.GOLD + "Timer");
//        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                if (!player.isOnline()) {
//                    cancel();
//                    return;
//                }
//
//                scoreboard.getEntries().forEach(scoreboard::resetScores); // clear the previous scoreboard
//
////                if (EventHorizon.tournamentTimer == null) //if timer has not been started
////                    objective.getScore(ChatColor.WHITE + "01:00:00").setScore(0); //display timer in scoreboard
//
////                else //if time has been started
////                    objective.getScore(ChatColor.WHITE + TournamentTimer.timeDisplayed).setScore(0); //display timer in scoreboard
//
//                player.setScoreboard(scoreboard);
//            }
//        }.runTaskTimer(plugin, 0L, 20L); // repeats every second
//    }

}
