package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore.data.gen.neoforge.AbstractLanguageProvider;
import games.moegirl.sinocraft.sinocore_test.datagen.TestLangKeys;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.data.PackOutput;

public class TestEnLanguageProvider extends AbstractLanguageProvider {

    public TestEnLanguageProvider(PackOutput output, String modId) {
        super(output, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        addTab(TestRegistry.TEST_TAB, "SinoTest: Test Tab");
        addItem(TestRegistry.TEST_ITEM_MC_TAB, "Test Item: In building tab");
        addItem(TestRegistry.TEST_ITEM_MOD_TAB, "Test Item: In mod tab");
        addItem(TestRegistry.TEST_ITEM_MOD_MC_TAB, "Test Item: In mod and building tab");
        addBlock(TestRegistry.TEST_BLOCK, "Test Block");
        add(TestLangKeys.TEST_WITH_ITEM, "Your offhand has item with test tag.");
        add(TestLangKeys.TEST_WITH_BLOCK, "You clicked a block with test tag.");
    }
}
