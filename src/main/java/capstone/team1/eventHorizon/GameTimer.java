package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.Utility.MsgUtil;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;


//class for the tournament timer that runs from the beginning to the end of the tournament
public class GameTimer extends BukkitRunnable {
    private GameTimer instance;
    private final EventHorizon plugin;
    public int remainingTime = -1;
    private final int eventInterval;


    public GameTimer(int duration, int eventInterval) {
        this.plugin = EventHorizon.getPlugin();
        this.instance = this;
        this.remainingTime = duration;
        this.eventInterval = eventInterval;
    }
  
    @Override
    public void run() {
        //stops timer is the remaining is less than 0
        if(remainingTime <= 0){
          endTimer();
          return;
        }
        remainingTime--;
        if (remainingTime % eventInterval == 0) {
            plugin.getEventManager().triggerEvent();
        }
    }

    public int endTimer(){
        this.cancel();
        return remainingTime;
    }

    public int getRemainingTime(){
        return remainingTime;
    }
}
