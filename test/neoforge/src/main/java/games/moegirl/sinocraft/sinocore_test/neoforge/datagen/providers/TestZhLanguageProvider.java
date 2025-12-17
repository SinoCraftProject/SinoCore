package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.AbstractLanguageProvider;
import games.moegirl.sinocraft.sinocore_test.datagen.TestLangKeys;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;
import net.minecraft.data.PackOutput;

public class TestZhLanguageProvider extends AbstractLanguageProvider {

    public TestZhLanguageProvider(PackOutput output, String modId) {
        super(output, modId, "zh_cn");
    }

    @Override
    protected void register() {
        addTab(TestRegistry.TEST_TAB, "SinoTest: 测试标签页");
        addItem(TestRegistry.TEST_ITEM_TAB_1, "测试物品一：位于SinoTest Mod标签页");
        addItem(TestRegistry.TEST_ITEM_TAB_2, "测试物品二：位于SinoTest Mod标签页 + 原版建筑方块标签页");
        addBlock(TestRegistry.TEST_BLOCK, "测试方块");
        add(TestLangKeys.TEST_WITH_ITEM, "你的副手物品携带了测试标签。");
        add(TestLangKeys.TEST_WITH_BLOCK, "你点击的方块带有测试标签。");

        add("sinocoretest.key_only_in_zh_cn", "仅在中文语言文件中存在的翻译键，AbstractLanguageProvider会对此提出警告。");
    }
}
