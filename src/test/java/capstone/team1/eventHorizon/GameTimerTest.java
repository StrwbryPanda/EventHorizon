package capstone.team1.eventHorizon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTimerTest {

    @BeforeEach
    void setup() {
        EventHorizon.setPlugin(null);
    }

    @Test
    void testConstructorSetsInitialDuration() {
        GameTimer timer = new GameTimer(60, 10);
        assertEquals(60, timer.getRemainingTime());
    }

    @Test
    void testRunDecrementsRemainingTime() {
        GameTimer timer = new GameTimer(10, 5);
        timer.run();  // simulate one tick
        assertEquals(9, timer.getRemainingTime(), "Timer should decrement by 1");
    }
}
