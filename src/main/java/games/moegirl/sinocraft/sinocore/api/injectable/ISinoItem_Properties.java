package games.moegirl.sinocraft.sinocore.api.injectable;

import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public interface ISinoItem_Properties {
    /**
     * 将物品放入某个 CreativeModeTab
     *
     * @param tab CreativeModeTab
     */
    default Item.Properties sino$tab(IRegRef<CreativeModeTab> tab) {
        return sino$tab(tab.getKey(), ItemStack::new);
    }

    /**
     * 将物品放入某个 CreativeModeTab
     *
     * @param tab CreativeModeTab
     */
    default Item.Properties sino$tab(ResourceKey<CreativeModeTab> tab) {
        return sino$tab(tab, ItemStack::new);
    }

    /**
     * 将物品放入某个 CreativeModeTab
     *
     * @param tab CreativeModeTab
     * @param sup ItemStack 构造方法
     */
    default Item.Properties sino$tab(IRegRef<CreativeModeTab> tab, Function<ItemLike, ItemStack> sup) {
        return sino$tab(tab.getKey(), sup);
    }

    /**
     * 将物品放入某个 CreativeModeTab
     *
     * @param tab CreativeModeTab
     * @param sup ItemStack 构造方法
     */
    default Item.Properties sino$tab(ResourceKey<CreativeModeTab> tab, Function<ItemLike, ItemStack> sup) {
        return (Item.Properties) this;
    }

    /**
     * 获取所有应加入此物品的 CreativeModeTab
     */
    default List<Pair<ResourceKey<CreativeModeTab>, Function<ItemLike, ItemStack>>> sino$getTabs() {
        return List.of();
    }
}
