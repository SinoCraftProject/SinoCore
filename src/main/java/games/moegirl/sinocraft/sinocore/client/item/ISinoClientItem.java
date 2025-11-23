package games.moegirl.sinocraft.sinocore.client.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

/**
 * Use {@link games.moegirl.sinocraft.sinocore.client.ClientRegister#registerItemCustomRenderer(Item, BlockEntityWithoutLevelRenderer)} instead.
 */
@Deprecated(forRemoval = true, since = "1.2.0")
public interface ISinoClientItem {
    default BlockEntityWithoutLevelRenderer sino$getCustomRender() {
        return null;
    }
}
