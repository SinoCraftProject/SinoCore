package games.moegirl.sinocraft.sinocore_test.neoforge.datagen;

import games.moegirl.sinocraft.sinocore.data.gen.DataGenContext;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TestBlockStateProvider extends BlockStateProvider {

    public TestBlockStateProvider(DataGenContext context) {
        super(context.getOutput(), context.getModId(), (ExistingFileHelper) context.getExistingFileHelper());
    }

    @Override
    protected void registerStatesAndModels() {
        cubeAll(TestRegistry.TEST_BLOCK.get());
        simpleBlock(TestRegistry.TEST_BLOCK.get());
    }
}
