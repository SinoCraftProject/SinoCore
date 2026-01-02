package games.moegirl.sinocraft.sinocore.api.injectable;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;
import java.util.function.Supplier;

public interface ISinoCreativeModeTab_Builder {
    default CreativeModeTab.Builder sino$icon(Supplier<Supplier<? extends ItemLike>> icon) {
        Objects.requireNonNull(icon);
        return ((CreativeModeTab.Builder) this).icon(() -> new ItemStack(icon.get().get()));
    }
}
