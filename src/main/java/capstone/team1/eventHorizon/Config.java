package capstone.team1.eventHorizon;

import java.util.List;

public class Config
{
    public static int getTournamentTimer() {
        return EventHorizon.plugin.getConfig().getInt("tournament-timer");
    }

    public static int getEventFrequency() {
        return EventHorizon.plugin.getConfig().getInt("event-frequency");
    }

    public static void reloadConfig() {
        EventHorizon.plugin.reloadConfig();
    }

    public static boolean getScoreboardSetting() {
        return EventHorizon.plugin.getConfig().getBoolean("scoreboard-setting");
    }

    public static double getPosWeight() {
        return EventHorizon.plugin.getConfig().getDouble("event.posWeight", 1.0);
    }

    public static double getNegWeight() {
        return EventHorizon.plugin.getConfig().getDouble("event.negWeight", 0.0);
    }

    public static double getNeutralWeight() {
        return EventHorizon.plugin.getConfig().getDouble("event.neutralWeight", 0.0);
    }

    public static List<String> getEnabledEvents() {
        return EventHorizon.plugin.getConfig().getStringList("events.enabled");
    }

}
