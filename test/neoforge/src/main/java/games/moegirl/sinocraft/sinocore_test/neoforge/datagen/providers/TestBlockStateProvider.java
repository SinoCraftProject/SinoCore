package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.AbstractAutoGenBlockStateProvider;
import games.moegirl.sinocraft.sinocore.api.registry.IRegistry;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;

public class TestBlockStateProvider extends AbstractAutoGenBlockStateProvider {

    public TestBlockStateProvider(PackOutput output, String modId, ExistingFileHelper exFileHelper, List<IRegistry<? extends Block>> autoGenRegistries) {
        super(output, modId, exFileHelper, autoGenRegistries);
    }

    @Override
    protected void register() {
        cubeAll(TestRegistry.TEST_BLOCK.get());
        simpleBlock(TestRegistry.TEST_BLOCK.get());
    }
}
