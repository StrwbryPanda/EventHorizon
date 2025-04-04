package capstone.team1.eventHorizon.events.utility.fawe;

import com.destroystokyo.paper.MaterialSetTag;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.ArrayList;
import java.util.Collection;

public class BlockMasks
{
    private Collection<BlockType> groundBlocks;
    private Collection<BlockType> cannotBeReplaced;
    private Collection<BlockType> air;

    public BlockMasks() {
        initializeGroundBlocks();
        initializeCannotBeReplaced();
        air =  new ArrayList<>();
        air.addAll(getBlocksFromTag(MaterialSetTag.AIR)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#air

    }
    public Collection<BlockType> getBlocksFromTag(Tag<Material> tag) {
        Collection<BlockType> blocks = new ArrayList<>();
        tag.getValues().forEach((material) -> {
            blocks.add(BukkitAdapter.asBlockType(material));
        });
        return blocks;
    }

    public void initializeGroundBlocks()
    {
        groundBlocks = new ArrayList<>();
        groundBlocks.addAll(getBlocksFromTag(MaterialSetTag.SCULK_REPLACEABLE)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#sculk_replaceable
        groundBlocks.add(BukkitAdapter.asBlockType(Material.SNOW_BLOCK));
        groundBlocks.add(BukkitAdapter.asBlockType(Material.POWDER_SNOW));
    }

    public void initializeCannotBeReplaced(){
        cannotBeReplaced = new ArrayList<>();
        //
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.FEATURES_CANNOT_REPLACE)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#features_cannot_replace
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.WITHER_IMMUNE)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#wither_immune
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.SHULKER_BOXES)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#shulker_boxes
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.MOB_INTERACTABLE_DOORS)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#mob_interactable_doors
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.TRAPDOORS)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#trapdoors
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.ANVIL)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#anvil
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.BEDS)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#beds
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.ALL_SIGNS)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#all_signs
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.RAILS)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#rails
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.PORTALS)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#portals
        cannotBeReplaced.addAll(getBlocksFromTag(MaterialSetTag.AIR)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#air


        //blocks with GUI
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.BARREL));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.BEACON));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.BLAST_FURNACE));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.BREWING_STAND));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.CARTOGRAPHY_TABLE));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.COMMAND_BLOCK));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.CHAIN_COMMAND_BLOCK));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.REPEATING_COMMAND_BLOCK));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.CHEST));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.CRAFTER));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.DISPENSER));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.DROPPER));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.ENCHANTING_TABLE));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.ENDER_CHEST));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.FURNACE));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.GRINDSTONE));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.HOPPER));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.LECTERN));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.LOOM));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.SMITHING_TABLE));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.SMOKER));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.STONECUTTER));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.STRUCTURE_BLOCK));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.TRAPPED_CHEST));

        //special blocks
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.BARRIER));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.BEDROCK));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.DECORATED_POT));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.END_PORTAL_FRAME));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.JIGSAW));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.LIGHT));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.MOVING_PISTON));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.REINFORCED_DEEPSLATE));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.SPAWNER));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.TRIAL_SPAWNER));
        cannotBeReplaced.add(BukkitAdapter.asBlockType(Material.VAULT));

    }

    public Collection<BlockType> getGroundBlocks()
    {
        return groundBlocks;
    }

    public Collection<BlockType> getCannotBeReplaced()
    {
        return cannotBeReplaced;
    }

    public Collection<BlockType> getAir()
    {
        return air;
    }
}
