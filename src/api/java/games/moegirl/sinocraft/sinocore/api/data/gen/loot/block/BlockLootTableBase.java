package games.moegirl.sinocraft.sinocore.api.data.gen.loot.block;

import games.moegirl.sinocraft.sinocore.api.SinoCoreAPI;
import games.moegirl.sinocraft.sinocore.api.block.ILootableBlock;
import games.moegirl.sinocraft.sinocore.api.util.BlockLootables;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.Registry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author skyinr
 */
public class BlockLootTableBase extends BlockLoot {
    private final String modID;
    private final Set<Block> knownBlocks = new ObjectOpenHashSet<>();
    private final Set<Block> toSkip = new ObjectOpenHashSet<>();

    /**
     * @param modID  mod id
     * @param blocks skip blocks
     */
    public BlockLootTableBase(String modID, Block... blocks) {
        this.modID = modID;
        skip(blocks);
    }

    /**
     * Add a block to the list of blocks
     *
     * @param block add this block to the list
     * @param table the loot table to use for this block
     */
    @Override
    protected void add(Block block, LootTable.Builder table) {
        super.add(block, table);
        knownBlocks.add(block);
    }

    /**
     * Get the list of blocks
     *
     * @return known blocks
     */
    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }

    /**
     * Add a block to the list of blocks to skip
     *
     * @param blocks blocks to skip
     */
    protected void skip(Block... blocks) {
        Collections.addAll(toSkip, blocks);
    }

    protected void skip(Collection<Block> blocks) {
        toSkip.addAll(blocks);
    }

    /**
     * Should this block be skipped?
     *
     * @param block block to check
     * @return should this block be skipped?
     */
    protected boolean skipBlock(Block block) {
        return knownBlocks.contains(block) || toSkip.contains(block);
    }

    /**
     * Build a loot table for a block
     *
     * @param blocks blocks to register
     */
    protected void dropSelfWithContents(Set<Block> blocks) {
        for (Block block : blocks) {
            // skyinr: Skip blocks
            if (skipBlock(block)) {
                continue;
            }
            if (block instanceof ILootableBlock block1) {
                if (block.getLootTable().equals(new ResourceLocation(block.getRegistryName().getNamespace(), "blocks/" + block.getRegistryName().getPath()))) {
                    SinoCoreAPI.LOGGER.atWarn().log("Do not use BlockBehaviour.Properties#lootFrom when ILootableBlock is implemented");
                }
                add(block, block1.createLootBuilder(BlockLootables.INSTANCE));
                continue;
            }
            add(block, LootTable.lootTable().withPool(applyExplosionDecay(block, LootPool.lootPool()
                            .name("main")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(block))
                    ))
            );
        }
    }

    @Override
    protected void addTables() {
        dropSelfWithContents(Registry.BLOCK.stream()
                .filter(b -> modID.equals(Registry.BLOCK.getKey(b).getNamespace()))// skyinr: Filter block in mod
                .collect(Collectors.toSet()));
    }
}
