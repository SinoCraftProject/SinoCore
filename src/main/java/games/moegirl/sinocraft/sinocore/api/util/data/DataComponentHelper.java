package games.moegirl.sinocraft.sinocore.api.util.data;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class DataComponentHelper {
    public static <T> T getOrDefault(ItemStack stack, DataComponentType<T> type, T value) {
        return stack.getOrDefault(type, stack.getPrototype().getOrDefault(type, value));
    }

    public static DyedItemColor getDyedColor(ItemStack stack, DyedItemColor value) {
        return getOrDefault(stack, DataComponents.DYED_COLOR, value);
    }
}
