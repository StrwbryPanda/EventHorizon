package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.events.EventFrequencyTimer;
import jdk.jfr.Event;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;


//class for the tournament timer that runs from the beginning to the end of the tournament
public class TournamentTimer extends BukkitRunnable {
    private final EventHorizon plugin;


    public TournamentTimer() {
        this.plugin = EventHorizon.plugin;

    }
  
    @Override
    public void run() {
        //stops timer is the remaining is less than 0
        if(remainingTime <= 0){
          end();
          return;
        }
        //displays the remaining time
        displayRemainingTime();
        remainingTime--;
    }



//format for timer
    public static String formatTime(int seconds){
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, sec);
    }

    public int remainingTime = -1;
    public boolean hasStarted = false;
    public boolean isPaused = false;
    public int duration = -1;

    public boolean start(int duration) {
        if (hasStarted && !isPaused) {
            return false;
        }
        this.duration = duration;
        this.remainingTime = duration;
        hasStarted = true;
        this.runTaskTimerAsynchronously(plugin, 0, 20);
        return true;
    }



    public void pause(){
        this.cancel();
        isPaused = true;
    }

    public void resume(){
        if(remainingTime == -1 || !hasStarted){
            return;
        }
        this.start(remainingTime);
        isPaused = false;
    }

    public void end(){
        this.cancel();
        hasStarted = false;
        isPaused = false;
        Util.broadcast("<red>Tournament has ended");
    }


    public void displayRemainingTime(){
        Util.broadcast("<red>Tournament Time Remaining: <aqua>" + formatTime(remainingTime));
    }
}
