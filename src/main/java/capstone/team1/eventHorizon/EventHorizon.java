package capstone.team1.eventHorizon;

<<<<<<< Updated upstream
=======
import capstone.team1.eventHorizon.commands.CommandsManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnstableApiUsage")

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
        plugin = this;

        // Plugin startup logic
        capstone.team1.eventHorizon.ScoreboardManager scoreboardManager = new ScoreboardManager(this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this, scoreboardManager), this);

        saveResource("config.yml", /* replace */ false);

        //initializes eventhorizon base command
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> event.registrar().register("eventhorizon", new CommandsManager(this)));
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
