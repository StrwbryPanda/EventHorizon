package capstone.team1.eventHorizon;

    import me.clip.placeholderapi.expansion.PlaceholderExpansion;
    import org.bukkit.OfflinePlayer;
    import org.jetbrains.annotations.NotNull;

    /**
     * PlaceholderEventHorizon is a PlaceholderAPI expansion that provides placeholders
     * for the EventHorizon plugin's tournament timer information.
     */
    public class PlaceholderEventHorizon extends PlaceholderExpansion
    {
        /**
         * Gets the author(s) of the expansion.
         * @return A string containing the expansion authors
         */
        @SuppressWarnings("UnstableApiUsage")
        @Override
        @NotNull
        public String getAuthor() {
            return String.join(", ", EventHorizon.getPlugin().getPluginMeta().getAuthors());
        }

        /**
         * Gets the identifier used for this expansion in placeholders.
         * @return The identifier string "eventhorizon"
         */
        @Override
        @NotNull
        public String getIdentifier() {
            return "eventhorizon";
        }

        /**
         * Gets the version of the expansion.
         * @return The expansion version string
         */
        @SuppressWarnings("UnstableApiUsage")
        @Override
        @NotNull
        public String getVersion() {
            return EventHorizon.getPlugin().getPluginMeta().getVersion();
        }

        /**
         * Determines if the expansion should persist through reloads.
         * @return true to persist through reloads
         */
        @Override
        public boolean persist() {
            return true;
        }

        /**
         * Processes placeholder requests for this expansion.
         * @param player The player to get the placeholder value for
         * @param params The placeholder identifier that was used
         * @return The value for the placeholder, or null if invalid
         */
        @Override
        public String onRequest(OfflinePlayer player, @NotNull String params) {
            if (params.equalsIgnoreCase("remainingtime")) {
                return "" + EventHorizon.getScheduler().getRemainingTime();
            }

            if (params.equalsIgnoreCase("remainingtime_formatted")) {
                return formatTime(EventHorizon.getScheduler().getRemainingTime());
            }

            return "Not started";
        }

        /**
         * Formats seconds into a HH:MM:SS time format.
         * @param seconds The number of seconds to format
         * @return A formatted string in the format "HH:MM:SS"
         */
        public String formatTime(int seconds){
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int sec = seconds % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, sec);
        }
    }