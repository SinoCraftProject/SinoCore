package games.moegirl.sinocraft.sinocore.neoforge.client;

import games.moegirl.sinocraft.sinocore.SinoCore;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Internal
@SuppressWarnings("deprecation")
@EventBusSubscriber(modid = SinoCore.MODID, value = Dist.CLIENT)
public class SinoClientNeoForge {
    private static ItemColors itemColors;
    private static BlockColors blockColors;

    public static void registerItemColor(ItemColor color, ItemLike... items) {
        if (itemColors == null) {
            throw new IllegalStateException("Call ClientRegister in your FMLClientInitializationEvent.");
        }

        itemColors.register(color, items);
    }

    public static void registerBlockColor(BlockColor color, Block... blocks) {
        if (blockColors == null) {
            throw new IllegalStateException("Call ClientRegister in your FMLClientInitializationEvent.");
        }

        blockColors.register(color, blocks);
    }

    @SubscribeEvent
    public static void onRegisterColorHandlersEvent(RegisterColorHandlersEvent.Item event) {
        itemColors = event.getItemColors();
    }

    @SubscribeEvent
    public static void onRegisterColorHandlersEvent(RegisterColorHandlersEvent.Block event) {
        blockColors = event.getBlockColors();
    }

    private static final Map<Item, BlockEntityWithoutLevelRenderer> itemCustomRenderers = new HashMap<>();

    public static void setItemCustomRenderer(Item item, BlockEntityWithoutLevelRenderer renderer) {
        itemCustomRenderers.put(item, renderer);
    }

    public static @Nullable BlockEntityWithoutLevelRenderer getItemCustomRenderer(Item item) {
        return itemCustomRenderers.get(item);
    }
}
