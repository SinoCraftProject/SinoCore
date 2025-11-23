package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore_test.datagen.TestTags;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TestItemTagsProvider extends ItemTagsProvider {
    public TestItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registries) {
        tag(TestTags.TEST_ITEM_TAG).add(TestRegistry.TEST_ITEM_MOD_TAB.getKey());
    }
}
