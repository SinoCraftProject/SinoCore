package games.moegirl.sinocraft.sinocore.api.injectable;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.function.Supplier;

public interface ISinoCreativeModeTab_Builder {
    default CreativeModeTab.Builder sino$icon(Supplier<Item> icon) {
        Objects.requireNonNull(icon);
        return ((CreativeModeTab.Builder) this).icon(() -> new ItemStack(icon.get()));
    }
}
