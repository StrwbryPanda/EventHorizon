package capstone.team1.eventHorizon.utility;

import capstone.team1.eventHorizon.EventHorizon;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;

import java.time.Duration;

public class MsgUtility
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
        audience.sendActionBar(Component.text("Selected Event: ", NamedTextColor.AQUA).append(parse(message).color(NamedTextColor.DARK_AQUA)));
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

    public static void showTitleWithDurations(Audience target, String titleText) {
        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3000), Duration.ofMillis(1000));
        // Using the times object this title will use 500ms to fade in, stay on screen for 3000ms and then fade out for 1000ms
        final Title title = Title.title(Component.text("Starting Event:", NamedTextColor.AQUA),Component.text(titleText, NamedTextColor.DARK_AQUA), times);

        // Send the title, you can also use Audience#clearTitle() to remove the title at any time
        target.showTitle(title);
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
