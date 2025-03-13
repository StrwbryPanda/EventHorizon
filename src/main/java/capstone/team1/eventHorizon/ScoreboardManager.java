package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final EventHorizon plugin;
    static org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
    static Scoreboard scoreboard = manager.getNewScoreboard();
    public static Objective objective = scoreboard.registerNewObjective("test", Criteria.DUMMY, ChatColor.GOLD + "Timer");

    public ScoreboardManager() {
        this.plugin = EventHorizon.plugin;
    }

    public void createScoreboard(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                // If scoreboard is turned off, remove it from the player's screen
                if (!CommandScoreboard.isScoreboardOn) {
                    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); // Clear scoreboard
                    return;
                }

                // Otherwise, display the scoreboard
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                // Clear previous scores
                scoreboard.getEntries().forEach(scoreboard::resetScores);

                // Update scoreboard based on timer state
                if (CommandsManager.tournamentTimer == null) {
                    objective.getScore(ChatColor.WHITE + TournamentTimer.formatTime(CommandStart.time)).setScore(0);
                } else if (!TournamentTimer.isRunning && CommandsManager.tournamentTimer != null && !CommandEnd.isEnded) {
                    objective.getScore(ChatColor.WHITE + TournamentTimer.formatTime(CommandPause.timeReamining)).setScore(0);
                } else if (TournamentTimer.isRunning && TournamentTimer.remainingTime >= 0 && !CommandEnd.isEnded) {
                    objective.getScore(ChatColor.WHITE + TournamentTimer.timeDisplayed).setScore(0);
                } else if (TournamentTimer.remainingTime < 0 && !CommandEnd.isEnded) {
                    objective.getScore(ChatColor.WHITE + "00:00:00").setScore(0);
                } else if (CommandEnd.isEnded && !TournamentTimer.isRunning) {
                    objective.getScore(ChatColor.WHITE + "00:00:00").setScore(0);
                }

                player.setScoreboard(scoreboard);
            }
        }.runTaskTimer(plugin, 0L, 20L); // repeats every second
    }
}
