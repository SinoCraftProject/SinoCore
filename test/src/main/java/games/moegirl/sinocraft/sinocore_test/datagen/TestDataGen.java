package games.moegirl.sinocraft.sinocore_test.datagen;

import games.moegirl.sinocraft.sinocore.data.gen.DataGeneratorManager;
import games.moegirl.sinocraft.sinocore.data.gen.ForgeProvider;
import games.moegirl.sinocraft.sinocore_test.SinoCoreTest;
import games.moegirl.sinocraft.sinocore_test.datagen.gen.*;

import java.util.function.Supplier;

public class TestDataGen {

    public static void registerAll() {
        registerProvider();
        registerData();
    }

    private static void registerProvider() {
        DataGeneratorManager.createDataGenerator(SinoCoreTest.MODID, gen -> {
            gen.put(TestEnLanguageProvider::new);
            gen.put(TestZhLanguageProvider::new);
            gen.put(TestAdvancementProvider::new);
            gen.put(TestBiomeModifierPriovider::new);
            Supplier<TestBlockTagsProvider> blockTags = gen.put(TestBlockTagsProvider::new);
            gen.put(TestFeatureProvider::new);
            gen.put(TestItemModelProvider::new);
            gen.put(ctx -> new TestItemTagsProvider(ctx, blockTags.get()));
            gen.put(TestLootTableProvider::new);
            gen.put(TestRecipeProvider::new);
            gen.put(ForgeProvider::new);
        });
    }

    private static void registerData() {
    }
}
