package capstone.team1.eventHorizon.events.mobSpawn;

import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.potion.*;

/**
 * Event that spawns a pack of wolves near players
 * Now with enhanced spawn location controls for caves, water, and lava
 */
public class WolfPack extends BaseMobSpawn {

    public WolfPack(Plugin plugin) {
        super(plugin, EntityType.WOLF);
        setMobCount(4)               // Spawn 4 wolves per player
                .setMaxSpawnRadius(15)
                .setMinSpawnRadius(5)
                .setYRadius(0, 10)  // Wolves will spawn within 10 blocks up or down from player
                .setAllowCaveSpawns(true)     // Allow wolves to spawn in caves
                .setAllowWaterSpawns(false)   // Wolves don't spawn in water
                .setAllowLavaSpawns(false)    // Wolves don't spawn in lava
                .setYRange(0, 255);           // Valid Y-range for spawning
    }

    /**
     * Creates a variant of the wolf pack with custom settings
     *
     * @param plugin The plugin instance
     * @param mobCount Number of wolves to spawn per player
     * @param allowCaves Whether wolves can spawn in caves
     * @return Configured WolfPack instance
     */
    public static WolfPack createCustomPack(Plugin plugin, int mobCount, boolean allowCaves) {
        WolfPack pack = new WolfPack(plugin);
        pack.setMobCount(mobCount)
                .setAllowCaveSpawns(allowCaves);
        return pack;
    }

    @Override
    protected void onMobSpawned(Entity entity, Player player) {
        if (entity instanceof Wolf) {
            Wolf wolf = (Wolf) entity;

            // Make wolves angry
            wolf.setAngry(true);
            wolf.setTarget(player);
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999, 1));
        }
    }
}