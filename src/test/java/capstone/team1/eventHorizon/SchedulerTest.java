package capstone.team1.eventHorizon;

import capstone.team1.eventHorizon.GameTimer;
import capstone.team1.eventHorizon.Scheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {
    Scheduler scheduler;
    GameTimer timer;

    @BeforeEach
    void setUp() {
        // Create Scheduler with dummy plugin + eventFrequency
        scheduler = new Scheduler(null, 5);

        // Inject a fake GameTimer that doesn't call Bukkit methods

        scheduler.setGameTimer(new GameTimer(60, 5) {
            boolean cancelled = false;

            @Override
            public void cancel() {
                cancelled = true;
            }

            @Override
            public void run() {
                // No-op for testing
            }

            @Override
            public int endTimer() {
                cancel(); // simulate behavior
                return -1;
            }
        });
    }

    @Test
    void testResumeWhenNotPaused() {
        boolean result = scheduler.resume(); // without pausing first
        assertFalse(result, "Resume should fail if not paused");
    }


    @Test
    void testEndFailsIfNoTimer() {
        scheduler.setGameTimer(null); // simulate already-ended timer
        boolean result = scheduler.end();
        assertFalse(result, "End should fail if no timer is set");
    }

    @Test
    void testPauseReturnsFalseWhenTimerHasStarted() {
        scheduler.hasStarted = false;
        assertEquals(false, scheduler.pause());
    }
}
