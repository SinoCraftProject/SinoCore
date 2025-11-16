package games.moegirl.sinocraft.sinocore_test.datagen.gen;

import games.moegirl.sinocraft.sinocore.data.gen.tag.AbstractItemTagsProvider;
import games.moegirl.sinocraft.sinocore.data.gen.DataGenContext;
import games.moegirl.sinocraft.sinocore_test.datagen.TestTags;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;

public class TestItemTagsProvider extends AbstractItemTagsProvider {

    public TestItemTagsProvider(DataGenContext context, TagsProvider<Block> blockTagsProvider) {
        super(context, blockTagsProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TestTags.TEST_ITEM_TAG).add(TestRegistry.TEST_ITEM_MOD_TAB.getKey());
    }
}
