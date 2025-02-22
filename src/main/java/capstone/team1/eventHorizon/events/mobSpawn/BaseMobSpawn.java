package capstone.team1.eventHorizon.events.mobSpawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class BaseMobSpawn
{
    protected final JavaPlugin plugin;
    protected int spawnAmount; // number of mobs to spawn
    protected EntityType mobType; // mob type
    protected final Random random;
    protected final Map<UUID, Set<UUID>> playerMobs; // tracks spawned mobs per player
    protected final Map<UUID, Long> lastSpawnTime; // tracks last spawn time per player

    protected int maxSpawnRadius = 10; // maximum radius for spawning mobs
    protected int minSpawnRadius = 5; // minimum radius for spawning mobs
    protected int maxSpawnAttempts = 10;  // maximum  number of spawn attempts
    protected int spawnInterval = 60; // default spawn interval in seconds

    public BaseMobSpawn(JavaPlugin plugin, EntityType defaultMobType) {
        this.plugin = plugin;
        this.spawnAmount = 5;
        this.mobType = defaultMobType;
        this.random = new Random();
        this.playerMobs = new ConcurrentHashMap<>();
        this.lastSpawnTime = new ConcurrentHashMap<>();
    }

    // sets the type of mob to spawn
    public void setMobType(EntityType type)
    {
        this.mobType = type;
    }

    public void setSpawnInterval(int seconds) {
        this.spawnInterval = seconds;
    }

    // spawns mobs around the players
    public int spawnMobs(Player[] players) {
        int totalSpawned = 0;
        for (Player player : players) {
            for (int i = 0; i < spawnAmount; i++) {
                Location spawnLocation = getSafeSpawnLocation(player.getWorld(), player.getLocation());
                if (spawnLocation != null) {
                    LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(spawnLocation, mobType, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    totalSpawned++;
                }
            }
        }
        return totalSpawned;
    }

    public void startContinuousSpawning() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    spawnMobs(new Player[]{player});
                }
            }
        }.runTaskTimer(plugin, 0, spawnInterval * 20L); // Convert seconds to ticks
    }

    private Location getSafeSpawnLocation(World world, Location origin) {
        int x = origin.getBlockX() + getRandomNumber();
        int z = origin.getBlockZ() + getRandomNumber();
        int y = world.getHighestBlockYAt(x, z);
        int attempts = maxSpawnAttempts;

        while (attempts > 0) {
            attempts--;
            Material material = world.getBlockAt(x, y, z).getType();
            Material below = world.getBlockAt(x, y - 1, z).getType();
            Material above = world.getBlockAt(x, y + 1, z).getType();

            if (material.isSolid()) {
                y++;
            } else if (below == Material.AIR) {
                y--;
            } else if (below == Material.WATER || below == Material.LAVA || above.isSolid()) {
                x = origin.getBlockX() + getRandomNumber();
                z = origin.getBlockZ() + getRandomNumber();
            } else {
                return new Location(world, x, y, z);
            }
        }
        return null;
    }

    private int getRandomNumber() {
        return random.nextInt((maxSpawnRadius - minSpawnRadius) * 2 + 1) - (maxSpawnRadius - minSpawnRadius);
    }
}
