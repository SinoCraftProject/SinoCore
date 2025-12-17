package games.moegirl.sinocraft.sinocore.api.registry;

import games.moegirl.sinocraft.sinocore.platform.TabPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class TabDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {
    /**
     * All DisplayItemsGenerator registered via {@link ITabRegistry}
     * {@link CreativeModeTab.DisplayItemsGenerator#accept} will automatically called in {@link CreativeModeTab.Builder}
     */
    private static final Map<ResourceKey<CreativeModeTab>, TabDisplayItemsGenerator> REGISTERED_TAB_GENERATORS = new ConcurrentHashMap<>();

    /**
     * DisplayItemsGenerator which is <b>not</b> registered via {@link ITabRegistry}
     * It should be
     */
    private static final Map<ResourceKey<CreativeModeTab>, TabDisplayItemsGenerator> UNREGISTERED_TAB_GENERATORS = new ConcurrentHashMap<>();

    public static void setRegisteredGenerators(ResourceKey<CreativeModeTab> key, TabDisplayItemsGenerator generator) {
        REGISTERED_TAB_GENERATORS.put(key, generator);
    }

    public static TabDisplayItemsGenerator getGenerator(ResourceKey<CreativeModeTab> key) {
        if (REGISTERED_TAB_GENERATORS.containsKey(key)) {
            return REGISTERED_TAB_GENERATORS.get(key);
        }

        if (!UNREGISTERED_TAB_GENERATORS.containsKey(key)) {
            var generator = new TabDisplayItemsGenerator();
            UNREGISTERED_TAB_GENERATORS.put(key, generator);
            TabPlatform.onAddTabDisplayItemsGenerator(key, generator);
        }
        return UNREGISTERED_TAB_GENERATORS.get(key);
    }

    public static Map<ResourceKey<CreativeModeTab>, TabDisplayItemsGenerator> getUnregisteredGenerators() {
        return UNREGISTERED_TAB_GENERATORS;
    }

    private final List<Supplier<ItemStack>> items = new ArrayList<>();

    @Override
    public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        items.forEach(sup -> output.accept(sup.get()));
    }

    /**
     * 向对应 CreativeModeTab 添加物品
     *
     * @param item 添加的物品
     * @return 该 TabItemGenerator
     */
    public synchronized TabDisplayItemsGenerator addItem(ItemLike item) {
        return addStack(new ItemStack(item));
    }

    /**
     * 向对应 CreativeModeTab 添加物品
     *
     * @param item 添加的物品
     * @return 该 TabItemGenerator
     */
    public synchronized TabDisplayItemsGenerator addItem(Supplier<? extends ItemLike> item) {
        return addStack(() -> new ItemStack(item.get()));
    }

    /**
     * 向对应 CreativeModeTab 添加物品
     *
     * @param item 添加的物品栈
     * @return 该 TabItemGenerator
     */
    public synchronized TabDisplayItemsGenerator addStack(ItemStack item) {
        return addStack(() -> item);
    }

    /**
     * 向对应 CreativeModeTab 添加物品
     *
     * @param item 添加的物品栈
     * @return 该 TabItemGenerator
     */
    public synchronized TabDisplayItemsGenerator addStack(Supplier<ItemStack> item) {
        items.add(item);
        return this;
    }
}
