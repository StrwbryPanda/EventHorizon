package capstone.team1.eventHorizon;

import com.mojang.brigadier.Message;
import org.bukkit.Bukkit;

public class Util
{
    public static void log(String message)
    {
        Bukkit.getLogger().info(message);
    }

    public static void warning(String message)
    {
        Bukkit.getLogger().warning(message);
    }
}
