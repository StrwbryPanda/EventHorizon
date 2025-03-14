package capstone.team1.eventHorizon;

import org.bukkit.scheduler.BukkitRunnable;


//class for the tournament timer that runs from the beginning to the end of the tournament
public class GameTimer extends BukkitRunnable {
    private GameTimer instance;
    private final EventHorizon plugin;
    public int remainingTime = -1;
    public boolean hasStarted = false;
    public boolean isPaused = false;
    public int duration = -1;

    public GameTimer() {
        this.plugin = EventHorizon.plugin;
        this.instance = this;
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

    public boolean pause(){
        if (!hasStarted || isPaused) {
            return false;
        }
        this.cancel();
        isPaused = true;
        return true;
    }

    public boolean resume(){
        if(remainingTime == -1 || !hasStarted){
            return false;
        }
        boolean hasStarted = this.start(remainingTime);
        if(hasStarted){
            isPaused = false;
        }
        return hasStarted;
    }

    public boolean end(){
        if (!hasStarted) {
            return false;
        }
        this.cancel();
        hasStarted = false;
        isPaused = false;
        Util.broadcast("<red>Tournament has ended");
        return true;
    }

    public void displayRemainingTime(){
        Util.broadcast("<red>Tournament Time Remaining: <aqua>" + formatTime(remainingTime));
    }
}
