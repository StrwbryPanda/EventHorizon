package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.Scheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandPauseTest {
    @Test
    public void testPauseMessageWhenTrue() {
        String result = CommandPause.getPauseMessage(true);
        assertEquals("Tournament has been paused", result);
    }

    @Test
    public void testPauseMessageWhenFalse() {
        String result = CommandPause.getPauseMessage(false);
        assertEquals("<red>ERROR: Cannot pause tournament", result);
    }

}