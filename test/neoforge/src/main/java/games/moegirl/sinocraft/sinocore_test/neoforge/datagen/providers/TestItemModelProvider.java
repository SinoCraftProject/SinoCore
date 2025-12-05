package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore.datagen.AbstractAutoGenItemModelProvider;
import games.moegirl.sinocraft.sinocore.registry.IRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TestItemModelProvider extends AbstractAutoGenItemModelProvider {

    @SafeVarargs
    public TestItemModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper,
                                 IRegistry<? extends Item>... autoGenRegistries) {
        super(output, modId, existingFileHelper, autoGenRegistries);
    }

    @Override
    protected void registerModels() {

    }
}
