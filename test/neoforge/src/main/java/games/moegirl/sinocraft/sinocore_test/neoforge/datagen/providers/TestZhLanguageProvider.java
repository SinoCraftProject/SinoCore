package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore_test.datagen.TestLangKeys;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class TestZhLanguageProvider extends LanguageProvider {

    public TestZhLanguageProvider(PackOutput output, String modid) {
        super(output, modid, "zh_cn");
    }

    @Override
    protected void addTranslations() {
//        addTab(TestRegistry.TEST_TAB, "SinoTest: 测试标签");  // Todo
        addItem(TestRegistry.TEST_ITEM_MC_TAB, "测试物品：位于建筑物品标签");
        addItem(TestRegistry.TEST_ITEM_MOD_TAB, "测试物品：位于 Mod 标签");
        addItem(TestRegistry.TEST_ITEM_MOD_MC_TAB, "测试物品：位于两个标签");
        addBlock(TestRegistry.TEST_BLOCK, "测试方块");
        add(TestLangKeys.TEST_WITH_ITEM, "你的副手物品携带了测试标签。");
        add(TestLangKeys.TEST_WITH_BLOCK, "你点击的方块带有测试标签。");
    }
}
