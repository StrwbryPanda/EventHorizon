package capstone.team1.eventHorizon.events.utility.fawe;

import com.destroystokyo.paper.MaterialSetTag;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Constructs a new BlockMasks instance and initializes all block collections.
 * This includes ground blocks, non-replaceable blocks, and air blocks.
 */
public class BlockMasks
{
    /**
     * Collection of block types that are considered to be ground blocks.
     * These are blocks that you would likely want replaced on the surface.
     */
    private Collection<BlockType> groundBlocks;
    /**
     * Collection of block types that cannot be replaced.
     * These blocks include various categories such containers,
     * GUI blocks, and special blocks that should be preserved.
     */
    private Collection<BlockType> cannotBeReplaced;
    /**
     * Collection of block types that are considered underground blocks.
     * These are blocks you would likely want to replace in a cave.
     */
    private Collection<BlockType> undergroundBlocks;
    /**
     * Collection of block types that are considered as air blocks.
     * These blocks are typically transparent and do not obstruct movement.
     */
    private final Collection<BlockType> air;

    /**
     * Constructs a new BlockMasks instance and initializes all block collections.
     * This includes ground blocks, non-replaceable blocks, and air blocks.
     */
    public BlockMasks() {
        initializeGroundBlocks();
        initializeCannotBeReplaced();
        initializeUndergroundBlocks();
        air =  new ArrayList<>();
        air.addAll(getBlocksFromTag(MaterialSetTag.AIR)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#air

    }

    /**
     * Converts a Bukkit Material Tag into a collection of WorldEdit BlockTypes.
     *
     * @param tag The Bukkit Material Tag to convert
     * @return A collection of WorldEdit BlockTypes corresponding to the input tag
     */
    public Collection<BlockType> getBlocksFromTag(Tag<Material> tag) {
        Collection<BlockType> blocks = new ArrayList<>();
        tag.getValues().forEach((material) -> {
            blocks.add(BukkitAdapter.asBlockType(material));
        });
        return blocks;
    }

    /**
     * Initializes the collection of ground blocks.
     * Includes sculk replaceable blocks, snow blocks, and powder snow.
     */
    public void initializeGroundBlocks()
    {
        groundBlocks = new ArrayList<>();
        groundBlocks.addAll(getBlocksFromTag(MaterialSetTag.SCULK_REPLACEABLE)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#sculk_replaceable
        groundBlocks.add(BukkitAdapter.asBlockType(Material.SNOW_BLOCK));
        groundBlocks.add(BukkitAdapter.asBlockType(Material.POWDER_SNOW));
    }

    /**
     * Initializes the collection of blocks that cannot be replaced.
     * This includes various block categories such as wither-immune blocks,
     * containers, GUI blocks, and special blocks that should be preserved.
     */
    public void initializeCannotBeReplaced(){
        cannotBeReplaced = new ArrayList<>();
        //blocks categorized into block tags from https://minecraft.wiki/w/Block_tag_(Java_Edition)
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

    /**
     * Initializes the collection of underground blocks.
     * These blocks are typically found in caves.
     */
    public void initializeUndergroundBlocks(){
        undergroundBlocks = new ArrayList<>();
        undergroundBlocks.addAll(getBlocksFromTag(MaterialSetTag.BASE_STONE_OVERWORLD)); //https://minecraft.wiki/w/Block_tag_(Java_Edition)#base_stone_overworld
    }

    /**
     * Gets the collection of blocks considered as ground blocks.
     *
     * @return Collection of ground block types
     */
    public Collection<BlockType> getGroundBlocks()
    {
        return groundBlocks;
    }

    /**
     * Gets the collection of blocks that cannot be replaced.
     *
     * @return Collection of non-replaceable block types
     */
    public Collection<BlockType> getCannotBeReplaced()
    {
        return cannotBeReplaced;
    }

    /**
     * Gets the collection of blocks considered as air blocks.
     *
     * @return Collection of air block types
     */
    public Collection<BlockType> getAir()
    {
        return air;
    }
    /**
     * Gets the collection of blocks considered as underground blocks.
     *
     * @return Collection of underground block types
     */
    public Collection<BlockType> getUndergroundBlocks()
    {
        return undergroundBlocks;
    }
}
