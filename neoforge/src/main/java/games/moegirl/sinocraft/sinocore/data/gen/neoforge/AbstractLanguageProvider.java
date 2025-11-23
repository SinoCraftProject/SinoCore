package games.moegirl.sinocraft.sinocore.data.gen.neoforge;

import games.moegirl.sinocraft.sinocore.registry.ITabRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLanguageProvider extends LanguageProvider {
    private final Logger logger = LoggerFactory.getLogger(AbstractLanguageProvider.class);

    public AbstractLanguageProvider(PackOutput output, String modId, String locale) {
        super(output, modId, locale);
    }

    protected void addTab(ResourceKey<CreativeModeTab> key, String name) {
        CreativeModeTab group = BuiltInRegistries.CREATIVE_MODE_TAB.get(key);
        // 检查是否已经注册
        if (group != null) {
            if (group.getDisplayName().getContents() instanceof TranslatableContents lang) {
                // get translation key from display component
                add(lang.getKey(), name);
            } else {
                // 若对应 Tab 不需要语言文件，直接跳过
                logger.warn("Skipped add language to a tab without translatable name: {}", name);
            }
        } else {
            // Tab 并未注册，只添加
            String lang = ITabRegistry.buildDefaultTranslationKey(key);
            logger.warn("Add language to unregistered tab {}: {} = {}.", key.location(), lang, name);
            add(lang, name);
        }
    }
}
