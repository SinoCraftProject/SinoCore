package games.moegirl.sinocraft.sinocore.api.data.gen;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * BlockStateProviderBase
 *
 * @author skyinr
 */
public class BlockStateProviderBase extends BlockStateProvider {
    private final DeferredRegister<? extends Block> deferredRegister;
    public BlockStateProviderBase(DataGenerator generator, String modId, ExistingFileHelper existingFileHelper, DeferredRegister<? extends Block> deferredRegister) {
        super(generator, modId, existingFileHelper);
        this.deferredRegister = deferredRegister;
    }

    @Override
    protected void registerStatesAndModels() {
        Set<Block> blocks = getBlocks();
        blocks = skipBlock(blocks);

        registerBlock(blocks);
    }

    protected Set<Block> getBlocks() {
        // skyinr: Register models and state for blocks
        return deferredRegister.getEntries().stream().map(RegistryObject::get).collect(Collectors.toSet());
    }

    protected void registerBlock(Set<Block> blocks) {
        blocks.forEach(this::simpleBlock);
    }

    protected Set<Block> skipBlock(Set<Block> blocks) {
        return blocks;
    }

}
