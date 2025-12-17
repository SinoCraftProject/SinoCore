package games.moegirl.sinocraft.sinocore.api.registry;

import games.moegirl.sinocraft.sinocore.api.util.TranslationKeyHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;

/**
 * CreativeModeTab 注册表
 */
public interface ITabRegistry extends IRegistryBase<CreativeModeTab> {
    /**
     * Create a CreativeModeTab with default Builder
     *
     * @param name Name
     * @return CreativeModeTab register reference
     */
    default IRegRef<CreativeModeTab> register(String name) {
        return register(name, tab -> {
        });
    }

    /**
     * Create a CreativeModeTab with default Builder + custom builder
     *
     * @param name    Name
     * @param builder Builder
     * @return CreativeModeTab register reference
     */
    default IRegRef<CreativeModeTab> register(String name, Consumer<CreativeModeTab.Builder> builder) {
        var generator = new TabDisplayItemsGenerator();
        var ref = registerTab(name, tab -> {
            tab.title(Component.translatable(TranslationKeyHelper.buildDefaultTranslationKey(modId(), name)));
            tab.displayItems(generator);
            builder.accept(tab);
        });
        TabDisplayItemsGenerator.setRegisteredGenerators(ref.getKey(), generator);
        return ref;
    }

    /**
     * Create a CreativeModeTab with custom builder
     *
     * @param name     Name
     * @param consumer Builder consumer
     * @return CreativeModeTab register reference
     */
    IRegRef<CreativeModeTab> registerTab(String name, Consumer<CreativeModeTab.Builder> consumer);
}
