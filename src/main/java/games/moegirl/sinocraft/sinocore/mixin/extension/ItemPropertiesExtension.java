package games.moegirl.sinocraft.sinocore.mixin.extension;

import games.moegirl.sinocraft.sinocore.api.injectable.ISinoItem_Properties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(Item.Properties.class)
public abstract class ItemPropertiesExtension implements ISinoItem_Properties {

    @Unique
    private final List<Pair<ResourceKey<CreativeModeTab>, Function<ItemLike, ItemStack>>> sino$tabs = new ArrayList<>();

    @Override
    public Item.@NotNull Properties sino$tab(ResourceKey<CreativeModeTab> tab, Function<ItemLike, ItemStack> sup) {
        Objects.requireNonNull(tab);
        Objects.requireNonNull(sup);
        sino$tabs.add(Pair.of(tab, sup));
        return sino$this();
    }

    @Override
    public @NotNull List<Pair<ResourceKey<CreativeModeTab>, Function<ItemLike, ItemStack>>> sino$getTabs() {
        return sino$tabs;
    }

    @Unique
    private Item.Properties sino$this() {
        return (Item.Properties) (Object) this;
    }
}
