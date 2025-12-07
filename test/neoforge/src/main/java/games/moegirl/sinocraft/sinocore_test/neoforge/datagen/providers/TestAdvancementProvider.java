package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.advancement.AdvancementTree;
import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.AbstractAdvancementProvider;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class TestAdvancementProvider extends AbstractAdvancementProvider {

    public TestAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, String modId) {
        super(output, registries, existingFileHelper, modId);
    }

    @Override
    protected void register() {
        addAdvancementTree(saver -> new AdvancementTree(saver, modLoc("test_adv"), Advancement.Builder.advancement()
                .display(new ItemStack(TestRegistry.TEST_ITEM_MC_TAB.get()),
                        Component.literal("测试成就"),
                        Component.literal("这是一个用于测试的成就"),
                        null, AdvancementType.TASK, false, true, false)
                .addCriterion("test", PlayerTrigger.TriggerInstance.tick())));
    }
}
