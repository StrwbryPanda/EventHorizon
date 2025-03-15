package capstone.team1.eventHorizon;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderEventHorizon extends PlaceholderExpansion
{

    private final EventHorizon plugin;

    public PlaceholderEventHorizon() {
        this.plugin = EventHorizon.getPlugin();
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getPluginMeta().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "eventhorizon";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    @NotNull
    public String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("remainingtime")) {
            return "" + plugin.scheduler.getRemainingTime();
        }

        if (params.equalsIgnoreCase("remainingtime_formatted")) {
            return formatTime(plugin.scheduler.getRemainingTime());
        }

        return null;
    }

    public String formatTime(int seconds){
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, sec);
    }
}
