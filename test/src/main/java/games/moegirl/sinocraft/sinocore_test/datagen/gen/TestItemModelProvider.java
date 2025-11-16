package games.moegirl.sinocraft.sinocore_test.datagen.gen;

import games.moegirl.sinocraft.sinocore.data.gen.model.AbstractItemModelProvider;
import games.moegirl.sinocraft.sinocore.data.gen.DataGenContext;
import games.moegirl.sinocraft.sinocore.data.gen.delegate.ItemModelProviderDelegateBase;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;

public class TestItemModelProvider extends AbstractItemModelProvider {

    public TestItemModelProvider(DataGenContext context) {
        super(context, TestRegistry.ITEMS);
    }

    @Override
    public void generateModels(ItemModelProviderDelegateBase<?> delegate) {
    }
}
