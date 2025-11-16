package games.moegirl.sinocraft.sinocore_test.datagen.gen;

import games.moegirl.sinocraft.sinocore.data.gen.tag.AbstractBlockTagsProvider;
import games.moegirl.sinocraft.sinocore.data.gen.DataGenContext;
import games.moegirl.sinocraft.sinocore_test.datagen.TestTags;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.core.HolderLookup;

public class TestBlockTagsProvider extends AbstractBlockTagsProvider {

    public TestBlockTagsProvider(DataGenContext context) {
        super(context);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TestTags.TEST_BLOCK_TAG).add(TestRegistry.TEST_BLOCK.getKey());
    }
}
