package games.moegirl.sinocraft.sinocore.api.tree;

import games.moegirl.sinocraft.sinocore.api.utility.BlockLootables;
import games.moegirl.sinocraft.sinocore.api.block.ILootableBlock;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * A loot table for tree blocks
 */
public class TreeBlockLoot extends BlockLoot {
    private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};

    private final TreeBlocks blocks;
    public final Set<Block> addedBlocks = new HashSet<>();

    public TreeBlockLoot(TreeBlocks blocks) {
        this.blocks = blocks;
    }

    @Override
    protected void addTables() {
        addDrop(blocks.planks(), BlockLoot::createSingleItemTable);
        addDrop(blocks.sapling(), BlockLoot::createSingleItemTable);
        addDrop(blocks.log(), BlockLoot::createSingleItemTable);
        addDrop(blocks.strippedLog(), BlockLoot::createSingleItemTable);
        addDrop(blocks.strippedLog(), BlockLoot::createSingleItemTable);
        addDrop(blocks.wood(), BlockLoot::createSingleItemTable);
        addDrop(blocks.strippedWoods(), BlockLoot::createSingleItemTable);
        addDrop(blocks.leaves(), b -> BlockLoot.createLeavesDrops(b, blocks.sapling(), NORMAL_LEAVES_SAPLING_CHANCES));
        addDrop(blocks.sign(), BlockLoot::createSingleItemTable);
        addDrop(blocks.wallSign(), b -> BlockLoot.createSingleItemTable(blocks.sign()));
        addDrop(blocks.pressurePlate(), BlockLoot::createSingleItemTable);
        addDrop(blocks.trapdoor(), BlockLoot::createSingleItemTable);
        addDrop(blocks.stairs(), BlockLoot::createSingleItemTable);
        addDrop(blocks.pottedSapling(), b -> BlockLoot.createPotFlowerItemTable(blocks.sapling()));
        addDrop(blocks.button(), BlockLoot::createSingleItemTable);
        addDrop(blocks.slab(), BlockLoot::createSlabItemTable);
        addDrop(blocks.fenceGate(), BlockLoot::createSingleItemTable);
        addDrop(blocks.fence(), BlockLoot::createSingleItemTable);
        addDrop(blocks.door(), BlockLoot::createDoorTable);
        if (blocks.hasChest()) {
            addDrop(blocks.chest(), BlockLoot::createNameableBlockEntityTable);
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return addedBlocks;
    }

    /**
     * Return added blocks by the loot table, use for block filter
     *
     * @return blocks
     */
    public Set<Block> knownBlocks() {
        return Set.copyOf(addedBlocks);
    }

    private void addDrop(Block block, Function<Block, LootTable.Builder> drop) {
        if (block instanceof ILootableBlock lootable) {
            add(block, lootable.createLootBuilder(BlockLootables.INSTANCE));
        } else {
            add(block, drop);
        }
        addedBlocks.add(block);
    }
}
