package games.moegirl.sinocraft.sinocore_test.neoforge.datagen;

import games.moegirl.sinocraft.sinocore.data.gen.ForgeProvider;
import games.moegirl.sinocraft.sinocore.data.gen.DataGenContext;
import games.moegirl.sinocraft.sinocore_test.SinoCoreTest;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestForgeProviders implements ForgeProvider.IForgeProviders {

    @Override
    public @NotNull String getModId() {
        return SinoCoreTest.MODID;
    }

    @Override
    public @NotNull List<DataProvider> allProviders(DataGenContext context) {
        return List.of(new TestBlockStateProvider(context));
    }
}
