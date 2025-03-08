package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final JavaPlugin plugin;
    static org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
    static Scoreboard scoreboard = manager.getNewScoreboard();
    public static Objective objective = scoreboard.registerNewObjective("test", Criteria.DUMMY, ChatColor.GOLD + "Timer");


    //look at setscore on line 37
    public ScoreboardManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(Player player) {
        if (CommandScoreboard.isScoreboardOn)
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        else
            objective.setDisplaySlot(null);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                // clear the previous scoreboard
                scoreboard.getEntries().forEach(scoreboard::resetScores);


                if (CommandsManager.tournamentTimer == null){ //timer has not been started
                    objective.getScore(ChatColor.WHITE + TournamentTimer.formatTime(CommandStart.time)).setScore(0);
                }
                else if(!TournamentTimer.isRunning && CommandsManager.tournamentTimer != null && !CommandEnd.isEnded){ //timer is paused
                    objective.getScore(ChatColor.WHITE + TournamentTimer.formatTime(CommandPause.timeReamining)).setScore(0);
                }
                //if time has been started
                else if (TournamentTimer.isRunning && TournamentTimer.remainingTime >= 0 && !CommandEnd.isEnded){ //timer is running
                    objective.getScore(ChatColor.WHITE + TournamentTimer.timeDisplayed).setScore(0);
                }
                else if(TournamentTimer.remainingTime < 0 && !CommandEnd.isEnded){ //when time runs out
                    objective.getScore(ChatColor.WHITE + "00:00:00").setScore(0);
                }
                else if (CommandEnd.isEnded && !TournamentTimer.isRunning){
                    objective.getScore(ChatColor.WHITE + "00:00:00").setScore(0);
                }
                    player.setScoreboard(scoreboard);
           }
        }.runTaskTimer(plugin, 0L, 20L); // repeats every second
    }

}
