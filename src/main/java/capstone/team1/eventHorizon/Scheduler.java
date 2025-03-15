package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.utility.MsgUtil;
import capstone.team1.eventHorizon.events.EventManager;

public class Scheduler {
    private final int eventFrequency;
    private final EventManager eventManager;
    private final EventHorizon plugin;
    private GameTimer gameTimer;

    public boolean hasStarted = false;
    public boolean isPaused = false;
    public int pausedTime = -1;



    public Scheduler(EventManager eventManager) {
        this.plugin = EventHorizon.getPlugin();
        this.eventFrequency = Config.getEventFrequency();
        this.eventManager = eventManager;
    }

    public boolean start(int duration) {
        if (hasStarted && !isPaused) {
            return false;
        }
        gameTimer = new GameTimer(duration, eventFrequency);
        gameTimer.runTaskTimerAsynchronously(plugin, 0, 20);
        hasStarted = true;
        return true;
    }

    public boolean pause(){
        if (!hasStarted || isPaused) {
            return false;
        }
        pausedTime = gameTimer.endTimer();
        isPaused = true;
        return true;
    }

    public boolean resume(){
        if(pausedTime == -1 || !hasStarted){
            return false;
        }
        boolean hasStarted = this.start(pausedTime);
        if(hasStarted){
            isPaused = false;
        }
        return hasStarted;
    }

    public boolean end(){
        if (!hasStarted) {
            return false;
        }
        gameTimer.endTimer();
        hasStarted = false;
        isPaused = false;
        MsgUtil.broadcast("<red>Tournament has ended");
        return true;
    }

    public int getRemainingTime(){
        return gameTimer.getRemainingTime();
    }


}