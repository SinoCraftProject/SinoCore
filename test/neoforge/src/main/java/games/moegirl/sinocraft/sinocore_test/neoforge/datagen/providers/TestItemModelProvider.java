package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.AbstractAutoGenItemModelProvider;
import games.moegirl.sinocraft.sinocore.api.registry.IRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;

public class TestItemModelProvider extends AbstractAutoGenItemModelProvider {

    public TestItemModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper, List<IRegistry<? extends Item>> autoGenRegistries) {
        super(output, modId, existingFileHelper, autoGenRegistries);
    }

    @Override
    protected void register() {
    }
}
