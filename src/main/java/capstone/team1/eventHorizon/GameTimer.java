package capstone.team1.eventHorizon;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * A timer implementation for managing tournament duration and triggering events at specified intervals.
 * This class extends BukkitRunnable to handle asynchronous timing operations.
 */
public class GameTimer extends BukkitRunnable {
    private GameTimer instance;
    /** The remaining time in seconds. Initialized to -1 when no timer is running. */
    public int remainingTime = -1;
    /** The interval in seconds between event triggers. */
    private final int eventInterval;

    /**
     * Constructs a new GameTimer with the specified duration and event interval.
     *
     * @param duration The total duration of the timer in seconds
     * @param eventInterval The interval between events in seconds
     */
    public GameTimer(int duration, int eventInterval) {
        this.instance = this;
        this.remainingTime = duration;
        this.eventInterval = eventInterval;
    }

    /**
     * Executes on each tick of the timer.
     * Decrements the remaining time and triggers events at specified intervals.
     * If the remaining time reaches zero or below, the timer is stopped.
     */
    @Override
    public void run() {
        if(remainingTime <= 0){
            endTimer();
            return;
        }
        remainingTime--;
        if (remainingTime % eventInterval == 0) {
            EventHorizon.getEventManager().triggerEventBtWeight();
        }
    }

    /**
     * Stops the timer and returns the remaining time.
     *
     * @return The remaining time when the timer was stopped
     */
    public int endTimer(){
        this.cancel();
        return remainingTime;
    }

    /**
     * Gets the current remaining time of the timer.
     *
     * @return The remaining time in seconds, or -1 if the timer is not running
     */
    public int getRemainingTime(){
        return remainingTime;
    }
}