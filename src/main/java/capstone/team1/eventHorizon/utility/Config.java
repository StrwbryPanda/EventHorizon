package capstone.team1.eventHorizon.utility;

import capstone.team1.eventHorizon.EventHorizon;

import java.util.List;

public class Config
{
    public static int getTournamentTimer() {
        return EventHorizon.getPlugin().getConfig().getInt("tournament-timer");
    }
    public static int getEventFrequency() {
        return EventHorizon.getPlugin().getConfig().getInt("event.frequency");
    }
    public static boolean getScoreboardSetting() {
        return EventHorizon.getPlugin().getConfig().getBoolean("scoreboard-setting");
    }
    public static double getPosWeight() {
        return EventHorizon.getPlugin().getConfig().getDouble("event.posWeight");
    }
    public static double getNegWeight() {
        return EventHorizon.getPlugin().getConfig().getDouble("event.negWeight");
    }
    public static double getNeutralWeight() {
        return EventHorizon.getPlugin().getConfig().getDouble("event.neutralWeight");
    }
    public static List<String> getEnabledEvents() {
        return EventHorizon.getPlugin().getConfig().getStringList("enabled-events");
    }
    public static void reloadConfig() {
        EventHorizon.getPlugin().reloadConfig();
    }

}
