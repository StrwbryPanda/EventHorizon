package capstone.team1.eventHorizon;

public class Config
{
    public static int getTournamentTimer()
    {
        return EventHorizon.plugin.getConfig().getInt("tournament-timer");
    }

    public static int getEventFrequency() { return EventHorizon.plugin.getConfig().getInt("event-frequency");}


    public static void reloadConfig()
    {
        EventHorizon.plugin.reloadConfig();
    }
}
