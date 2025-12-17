package games.moegirl.sinocraft.sinocore.neoforge.api.datagen;

import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.ITabRegistry;
import games.moegirl.sinocraft.sinocore.api.util.TranslationKeyHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

public abstract class AbstractLanguageProvider extends LanguageProvider {
    public static final String LOCALE_EN_US ="en_us";

    protected static final Map<String, Map<String, List<String>>> KNOWN_LANG_KEYS = new HashMap<>();

    protected final String modId;
    protected final String locale;
    private final boolean strict;
    private final Logger logger;

    public AbstractLanguageProvider(PackOutput output, String modId, String locale) {
        this(output, modId, locale, false);
    }

    public AbstractLanguageProvider(PackOutput output, String modId, String locale, boolean strict) {
        super(output, modId, locale);
        this.modId = modId;
        this.locale = locale;
        this.strict = strict;
        this.logger = LoggerFactory.getLogger(getName());
    }

    // <editor-fold desc="Inner implementation">

    @Override
    protected final void addTranslations() {
        register();

        // Start: Checking language keys mismatch between locales.
        var currentKeys = getKeys();

        // Find baseline locale to compare.
        if (!KNOWN_LANG_KEYS.containsKey(modId)) {
            return;
        }
        var localeMap = KNOWN_LANG_KEYS.get(modId);
        var locales = localeMap.keySet();
        if (locales.size() <= 1) {
            return;
        }
        String baselineLocale = null;
        if (locales.contains(LOCALE_EN_US)) {   // Use en_us as baseline as default.
            baselineLocale = LOCALE_EN_US;
        } else {    // No en_us available, use the first one as baseline.
            for (var l : locales) {
                if (!locale.equals(l)) {
                    baselineLocale = l;
                    break;
                }
            }
        }
        if (baselineLocale == null) {   // No baseline is available, give up.
            return;
        }
        var baselineKeys = localeMap.get(baselineLocale);
        if (baselineKeys == null) {
            return;
        }

        // Do comparison.
        var missingKeys = baselineKeys.stream().filter(k -> !currentKeys.contains(k)).toList();
        var extraKeys = currentKeys.stream().filter(k -> !baselineKeys.contains(k)).toList();
        if (missingKeys.isEmpty() && extraKeys.isEmpty()) {
            return; // Good done!
        }

        logger.error("{} translation key(s) mismatch detected for mod {} locale {} with locale {}",
                missingKeys.size() + extraKeys.size(), modId, locale, baselineLocale);
        for (var key : missingKeys) {
            logger.error("- Missing key: {}", key);
        }
        for (var key : extraKeys) {
            logger.error("+ Extra key: {}", key);
        }

        if (strict) {
            throw new IllegalStateException("Translation keys mismatch");
        }
    }

    @Override
    public void add(String key, String value) {
        super.add(key, value);

        getKeys().add(key);
    }

    protected List<String> getKeys() {
        return KNOWN_LANG_KEYS.computeIfAbsent(modId, m -> new LinkedHashMap<>())
                .computeIfAbsent(locale, l -> new ArrayList<>());
    }

    protected @Nullable List<String> getKeysBaseline() {
        if (!KNOWN_LANG_KEYS.containsKey(modId)) {
            return null;
        }

        var localeMap = KNOWN_LANG_KEYS.get(modId);
        for (var entry : localeMap.entrySet()) {
            if (!locale.equals(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }

    // </editor-fold>

    protected abstract void register();

    protected void addTab(IRegRef<CreativeModeTab> key, String name) {
        add(key.getKey(), name);
    }

    protected void add(ResourceKey<CreativeModeTab> key, String name) {
        CreativeModeTab group = BuiltInRegistries.CREATIVE_MODE_TAB.get(key);
        // 检查是否已经注册
        if (group != null) {
            if (group.getDisplayName().getContents() instanceof TranslatableContents lang) {
                // get translation key from display component
                add(lang.getKey(), name);
            } else {
                // 若对应 Tab 不需要语言文件，给出Error级别的提示
                logger.error("No language key available for tab: {}, you must add it manually", key.location());
            }
        } else {
            // Tab 并未注册，只添加
            var itemKey = TranslationKeyHelper.buildDefaultTranslationKey(key);
            logger.warn("Add language to unregistered tab {}: {} = {}.", key.location(), itemKey, name);
            add(itemKey, name);
        }
    }
}
