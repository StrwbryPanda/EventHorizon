package capstone.team1.eventHorizon;

import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceholderEventHorizonTest {

    @Test
    public void formatTime_given90Seconds_returns000130() {
        assertEquals("00:01:30", PlaceholderEventHorizon.formatTime(90));
    }

    @Test
    public void formatTime_given0Seconds_returns000000() {
        assertEquals("00:00:00", PlaceholderEventHorizon.formatTime(0));
    }
}