package capstone.team1.eventHorizon.utility;

import capstone.team1.eventHorizon.EventHorizon;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.TextComponent;


public class MsgUtil
{
    public static void log(String message)
    {
        EventHorizon.getPlugin().getComponentLogger().info(parse(message));
    }

    public static void warning(String message)
    {
        EventHorizon.getPlugin().getComponentLogger().warn(parse(message));
    }

    public static void message(Audience audience, String message)
    {
        if (message.isEmpty())
            return;
        audience.sendMessage(parse(message));
    }

    /**
     * Sends an action bar to an audience
     *
     * @param audience the audience to send the actionbar to
     * @param message  the message to send supporting MiniMessage formatting
     */
    public static void actionBar(Audience audience, String message)
    {
        if (message.isEmpty())
        {
            return;
        }
        audience.sendActionBar(parse(message));
    }


    public static void title(String titleMessage)
    {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String fullmessage = titleMessage;
            TextComponent actionBarMessage = new TextComponent(fullmessage);
            actionBarMessage.setColor(ChatColor.AQUA);
            player.spigot().sendMessage(actionBarMessage);
        }

    }

    /**
     * Sends a sound to an audience
     *
     * @param audience the audience to send the sound to
     * @param sound    the adventure {@link Sound} to send
     */
    public static void sound(Audience audience, Sound sound)
    {
        audience.playSound(sound);
    }

    /**
     * Broadcast a message to all online players including the console
     *
     * @param message the message to send supporting MiniMessage formatting
     */
    public static void broadcast(String message)
    {
        Bukkit.getServer().sendRichMessage(message);
    }


    /**
     * Deserializes the message into an adventure {@link Component} and parses any MiniMessage placeholders including the plugin <prefix>
     *
     * @param message   the message to parse
     * @return A MiniMessage deserialized chat {@link Component}
     */
    private static Component parse(String message)
    {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(message);
    }
}
