package games.moegirl.sinocraft.sinocore.api.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class TranslationKeyHelper {
    /**
     * 生成默认 CreativeModeTab 标签名的语言文件键
     *
     * @param modId modId
     * @param name  tab名
     * @return CreativeModeTab 的语言键
     */
    public static String buildDefaultTranslationKey(String modId, String name) {
        return "itemGroup." + modId + "." + name;
    }

    public static String buildDefaultTranslationKey(ResourceKey<CreativeModeTab> name) {
        return buildDefaultTranslationKey(name.location().getNamespace(), name.location().getPath());
    }
}
