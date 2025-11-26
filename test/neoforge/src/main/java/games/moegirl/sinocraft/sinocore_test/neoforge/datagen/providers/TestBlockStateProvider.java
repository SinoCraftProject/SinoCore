package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TestBlockStateProvider extends BlockStateProvider {

    public TestBlockStateProvider(PackOutput output, String modId, ExistingFileHelper exFileHelper) {
        super(output, modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        cubeAll(TestRegistry.TEST_BLOCK.get());
        simpleBlock(TestRegistry.TEST_BLOCK.get());
    }
}
