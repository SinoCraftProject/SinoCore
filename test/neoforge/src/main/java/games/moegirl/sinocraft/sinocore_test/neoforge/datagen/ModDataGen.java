package games.moegirl.sinocraft.sinocore_test.neoforge.datagen;

import games.moegirl.sinocraft.sinocore_test.SinoCoreTest;
import games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers.*;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

@EventBusSubscriber(modid = SinoCoreTest.MODID)
public class ModDataGen {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var output = generator.getPackOutput();
        var registries = event.getLookupProvider();

        event.addProvider(new TestAdvancementProvider(output, registries, existingFileHelper, SinoCoreTest.MODID));
        event.addProvider(new TestEnLanguageProvider(output, SinoCoreTest.MODID));
        event.addProvider(new TestZhLanguageProvider(output, SinoCoreTest.MODID));
        event.addProvider(new TestBiomeModifierProvider(output, SinoCoreTest.MODID));   // Todo: biome modifiers
        var blockTags = event.addProvider(new TestBlockTagsProvider(output, registries, SinoCoreTest.MODID, existingFileHelper));
        event.addProvider(new TestItemTagsProvider(output, registries, blockTags.contentsGetter()));
        event.addProvider(new TestRecipeProvider(output, registries));
        event.addProvider(new TestBlockStateProvider(output, SinoCoreTest.MODID, existingFileHelper, List.of(TestRegistry.BLOCKS)));
        event.addProvider(new TestItemModelProvider(output, SinoCoreTest.MODID, existingFileHelper, List.of(TestRegistry.ITEMS)));
    }
}
