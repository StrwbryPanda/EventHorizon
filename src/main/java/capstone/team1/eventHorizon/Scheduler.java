package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.utility.Config;
import capstone.team1.eventHorizon.utility.MsgUtility;
import capstone.team1.eventHorizon.events.EventManager;

/**
 * The Scheduler class manages the timing and execution of events in the EventHorizon plugin.
 * It handles starting, pausing, resuming, and ending game sessions, as well as managing the event frequency.
 */
public class Scheduler {
    private int eventFrequency;
    private final EventHorizon plugin;
    private GameTimer gameTimer;

    public boolean hasStarted = false;
    public boolean isPaused = false;
    public int pausedTime = -1;

    /**
     * Constructor for the Scheduler class.
     */
    public Scheduler() {
        this.plugin = EventHorizon.getPlugin();
        this.eventFrequency = Config.getEventFrequency();
    }

    /**
     * Starts a new game timer with the specified duration.
     *
     * @param duration The duration of the game timer in seconds
     * @return true if the session was successfully started, false if a session is already running
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
     * Pauses the current game session.
     *
     * @return true if the session was successfully paused, false if no session is running or already paused
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
     * Resumes a paused game session.
     *
     * @return true if the session was successfully resumed, false if no session exists or couldn't be started
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
     * Ends the current game session.
     *
     * @return true if the session was successfully ended, false if no session was running
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
     * Gets the remaining time in the current game session.
     *
     * @return the remaining time in seconds, or -1 if no session is active
     */
    public int getRemainingTime() {
        return gameTimer.getRemainingTime();
    }
    public void reloadEventFrequency() {
        this.eventFrequency = Config.getEventFrequency();
    }
}