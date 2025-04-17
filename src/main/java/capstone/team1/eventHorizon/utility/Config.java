package capstone.team1.eventHorizon.utility;

import capstone.team1.eventHorizon.EventHorizon;

import java.util.List;

/**
 * Utility class for accessing configuration values from the plugin's config.yml file.
 * Provides static methods to retrieve various tournament and event settings.
 */
public class Config
{
    /**
     * Gets the total duration of the tournament in seconds.
     *
     * @return The tournament duration in seconds from config
     */
    public static int getTournamentTimer() {
        return EventHorizon.getPlugin().getConfig().getInt("tournament-timer");
    }

    /**
     * Gets the frequency at which events should occur during the tournament.
     *
     * @return The event frequency in seconds from config
     */
    public static int getEventFrequency() {
        return EventHorizon.getPlugin().getConfig().getInt("event.frequency");
    }

    /**
     * Gets the weight value for positive events in the event selection system.
     *
     * @return The positive event weight as a double from config
     */
    public static double getPosWeight() {
        return EventHorizon.getPlugin().getConfig().getDouble("event.posWeight");
    }

    /**
     * Gets the weight value for negative events in the event selection system.
     *
     * @return The negative event weight as a double from config
     */
    public static double getNegWeight() {
        return EventHorizon.getPlugin().getConfig().getDouble("event.negWeight");
    }

    /**
     * Gets the weight value for neutral events in the event selection system.
     *
     * @return The neutral event weight as a double from config
     */
    public static double getNeutralWeight() {
        return EventHorizon.getPlugin().getConfig().getDouble("event.neutralWeight");
    }

    /**
     * Gets the list of events that are currently enabled in the tournament.
     *
     * @return A List of String containing the enabled event names from config
     */
    public static List<String> getEnabledEvents() {
        return EventHorizon.getPlugin().getConfig().getStringList("enabled-events");
    }

    /**
     * Reloads the plugin's configuration from disk.
     * This will update all config values to their latest values from the config file.
     */
    public static void reloadConfig() {
        EventHorizon.getPlugin().reloadConfig();
    }
}