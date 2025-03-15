package capstone.team1.eventHorizon.events.itemSpawn;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import capstone.team1.eventHorizon.utility.MsgUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

import capstone.team1.eventHorizon.utility.MsgUtil;

public abstract class BaseItemSpawn extends BaseEvent {
    protected final Plugin plugin;
    protected final Random random = new Random();
    protected final NamespacedKey key;

    // Default configuration values
    public ItemStack itemType = new ItemStack(Material.STONE);
    private static final int DEFAULT_ITEM_COUNT = 5;
    private static final int DEFAULT_MAX_SPAWN_RADIUS = 20;
    private static final int DEFAULT_MIN_SPAWN_RADIUS = 3;
    private static final int DEFAULT_MAX_Y_RADIUS = 20;
    private static final int DEFAULT_MIN_Y_RADIUS = 3;
    private static final int DEFAULT_MAX_SPAWN_ATTEMPTS = 20;
    private static final int DEFAULT_SPAWN_INTERVAL = 60; // Seconds

    // Item properties
    public int itemCount = DEFAULT_ITEM_COUNT;
    public int maxSpawnRadius = DEFAULT_MAX_SPAWN_RADIUS;
    public int minSpawnRadius = DEFAULT_MIN_SPAWN_RADIUS;
    public int maxYRadius = DEFAULT_MAX_Y_RADIUS;
    public int minYRadius = DEFAULT_MIN_Y_RADIUS;
    public int maxSpawnAttempts = DEFAULT_MAX_SPAWN_ATTEMPTS;
    private int lastSpawnCount = 0;

    // Flags
    public boolean surfaceOnlySpawning = true;
    public boolean allowWaterSpawns = true;
    public boolean allowLavaSpawns = false;
    public boolean useContinuousSpawning = false;

    // Task management
    public BukkitTask continuousTask = null;
    public int spawnInterval = DEFAULT_SPAWN_INTERVAL;

    // Constructors
    public BaseItemSpawn(EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseItemSpawn(ItemStack defaultItemType, String eventName) {
        super(EventClassification.NEUTRAL, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.itemType = defaultItemType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    public BaseItemSpawn(ItemStack defaultItemType, EventClassification classification, String eventName) {
        super(classification, eventName);
        this.plugin = EventHorizon.getPlugin();
        this.itemType = defaultItemType;
        this.key = new NamespacedKey(plugin, this.eventName);
    }

    // Executes the event
    @Override
    public void execute() {
        try {
            this.lastSpawnCount = 0;

            if (useContinuousSpawning) {
                // Start continuous task for ongoing spawning
                boolean started = startContinuousTask();
                if (started) {
                    MsgUtil.log("Event " + eventName +
                            " started continuous spawning of " + itemType.toString() +
                            " items with interval of " + spawnInterval + " seconds");
                } else {
                    MsgUtil.log("Event " + eventName +
                            " tried to start continuous spawning but it was already running");
                }
            } else {
                // Do a one-time spawn for all players
                int spawned = spawnForAllPlayers();
                this.lastSpawnCount = spawned;

                MsgUtil.log("Event " + eventName +
                        " spawned " + spawned + " " + itemType.toString() +
                        " items across " + plugin.getServer().getOnlinePlayers().size() +
                        " players");
            }
        } catch (Exception e) {
            MsgUtil.warning("Error spawning items in " + eventName + ": " + e.getMessage());
        }
    }

    //  stops the event
    @Override
    public void terminate() {
        boolean stopped = stopContinuousTask();

        if (stopped) {
            MsgUtil.log("Event " + eventName + " stopped continuous spawning of " + itemType.toString() + " items");
        } else {
            MsgUtil.warning("Event " + eventName + " tried to stop continuous spawning but it was already stopped");
        }
    }

    //  Continuous task management
    public boolean startContinuousTask() {
        // Check if task is already running
        if (continuousTask != null && !continuousTask.isCancelled()) {
            return false;
        }

        // Use BukkitRunnable for continuous task that spawns items for all players
        continuousTask = new BukkitRunnable() {
            @Override
            public void run() {
                spawnForAllPlayers();
            }
        }.runTaskTimer(plugin, 20L, spawnInterval * 20L);

        return true;
    }

    public boolean stopContinuousTask() {
        // Check if there's a task to stop
        if (continuousTask == null || continuousTask.isCancelled()) {
            return false;
        }

        // Cancel the task
        continuousTask.cancel();
        continuousTask = null;

        return true;
    }

    public int spawnForAllPlayers() {
        MsgUtil.warning("spawnForAllPlayers() not implemented");
        return 0;
    }

}
