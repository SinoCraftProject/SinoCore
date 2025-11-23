package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TestAdvancementProvider extends AdvancementProvider {

    public TestAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders) {
        super(output, registries, existingFileHelper, subProviders);
    }

//    @Override // Todo
//    public void generateData(AdvancementProviderDelegateBase delegate) {
//        delegate.addAdvancementTree(saver -> new AdvancementTree(saver, modLoc("test_adv"), Advancement.Builder.advancement()
//                .display(new ItemStack(TestRegistry.TEST_ITEM_MC_TAB.get()),
//                        Component.literal("测试成就"),
//                        Component.literal("这是一个用于测试的成就"),
//                        null, AdvancementType.TASK, false, true, false)
//                .addCriterion("test", PlayerTrigger.TriggerInstance.tick()))
//        );
//    }
}
