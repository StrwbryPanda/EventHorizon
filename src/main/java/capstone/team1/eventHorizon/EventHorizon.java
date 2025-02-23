package capstone.team1.eventHorizon;

<<<<<<< HEAD
import capstone.team1.eventHorizon.commands.CommandsManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
=======
<<<<<<< Updated upstream
=======
import capstone.team1.eventHorizon.commands.CommandsManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
>>>>>>> parent of a70912b (Resetting main)
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnstableApiUsage")

<<<<<<< HEAD
public final class EventHorizon extends JavaPlugin {

    public static EventHorizon plugin;

    @Override
    public void onEnable() {
=======
@SuppressWarnings("UnstableApiUsage")

public final class EventHorizon extends JavaPlugin {
<<<<<<< Updated upstream
<<<<<<< Updated upstream

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveResource("config.yml", /* replace */ false);
=======
    public static EventHorizon plugin;
    @Override
    public void onEnable() {
=======
    public static EventHorizon plugin;
    @Override
    public void onEnable() {
>>>>>>> Stashed changes
>>>>>>> parent of a70912b (Resetting main)
        plugin = this;

        // Plugin startup logic
        capstone.team1.eventHorizon.ScoreboardManager scoreboardManager = new ScoreboardManager(this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this, scoreboardManager), this);

        saveResource("config.yml", /* replace */ false);

        //initializes eventhorizon base command
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> event.registrar().register("eventhorizon", new CommandsManager(this)));
<<<<<<< HEAD
=======
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
>>>>>>> parent of a70912b (Resetting main)
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
