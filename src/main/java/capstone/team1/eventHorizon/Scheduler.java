package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.utility.MsgUtility;
import capstone.team1.eventHorizon.events.EventManager;

/**
 * The Scheduler class manages the timing and execution of events in the EventHorizon plugin.
 * It handles starting, pausing, resuming, and ending the tournament game timer, as well as managing the event frequency.
 */
public class Scheduler {
    private final int eventFrequency;
    private final EventHorizon plugin;
    private GameTimer gameTimer;

    private boolean hasStarted = false;
    private boolean isPaused = false;
    private int pausedTime = -1;


    /**
     * Constructs a new Scheduler with the specified event manager.
     */
    public Scheduler() {
        this.plugin = EventHorizon.getPlugin();
        this.eventFrequency = Config.getEventFrequency();
    }

    /**
     * Starts the tournament timer with the specified duration.
     * @param duration The duration of the tournament in seconds
     * @return true if the timer was successfully started, false if the tournament is already running
     */
    public boolean start(int duration) {
        if (hasStarted && !isPaused) {
            return false;
        }
        gameTimer = new GameTimer(duration, eventFrequency);
        gameTimer.runTaskTimerAsynchronously(plugin, 0, 20);
        hasStarted = true;
        return true;
    }

    /**
     * Pauses the currently running tournament timer.
     * @return true if the timer was successfully paused, false if the tournament hasn't started or is already paused
     */
    public boolean pause() {
        if (!hasStarted || isPaused) {
            return false;
        }
        pausedTime = gameTimer.endTimer();
        isPaused = true;
        return true;
    }

    /**
     * Resumes a paused tournament timer.
     * @return true if the timer was successfully resumed, false if there's no paused tournament or the tournament hasn't started
     */
    public boolean resume() {
        if (pausedTime == -1 || !hasStarted) {
            return false;
        }
        boolean hasStarted = this.start(pausedTime);
        if (hasStarted) {
            isPaused = false;
        }
        return hasStarted;
    }

    /**
     * Ends the current tournament timer.
     * @return true if the tournament was successfully ended, false if no tournament was running
     */
    public boolean end() {
        if (!hasStarted) {
            return false;
        }
        gameTimer.endTimer();
        hasStarted = false;
        isPaused = false;
        MsgUtility.broadcast("<red>Tournament has ended");
        return true;
    }

    /**
     * Gets the remaining time in the current tournament.
     * @return the remaining time in seconds, or -1 if no tournament is running
     */
    public int getRemainingTime() {
        return gameTimer.getRemainingTime();
    }
}