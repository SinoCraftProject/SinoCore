package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore_test.datagen.TestTags;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TestBlockTagsProvider extends BlockTagsProvider {

    public TestBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TestTags.TEST_BLOCK_TAG).add(TestRegistry.TEST_BLOCK.getKey());
    }
}
