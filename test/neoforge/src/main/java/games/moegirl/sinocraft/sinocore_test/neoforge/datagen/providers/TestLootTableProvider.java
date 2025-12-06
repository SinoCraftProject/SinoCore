package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.AbstractLootTableProvider;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.CompletableFuture;

public class TestLootTableProvider extends AbstractLootTableProvider {

    public TestLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        super(output, registries, modId);
    }

    @Override
    protected void register() {
        addProvider(registries -> new AbstractBlockLootSubProvider(registries) {
            @Override
            public void generate() {
                add(TestRegistry.TEST_BLOCK.get(), createSelfDropDispatchTable(TestRegistry.TEST_BLOCK.get(),
                        hasSilkTouch(),
                        LootItem.lootTableItem(Items.APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))));
            }
        }, LootContextParamSets.BLOCK);
    }
}
