package games.moegirl.sinocraft.sinocore.client.platform.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ClientPlatformImpl {
    public static void registerItemModelPredicate(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function) {
        ItemProperties.register(item.asItem(), id, function);
    }

    public static void registerItemColor(ItemColor color, ItemLike... items) {
        ColorProviderRegistry.ITEM.register(color, items);
    }

    public static void registerBlockColor(BlockColor color, Block... blocks)  {
        ColorProviderRegistry.BLOCK.register(color, blocks);
    }

    public static void registerItemCustomRenderer(Item item, BlockEntityWithoutLevelRenderer renderer) {
        BuiltinItemRendererRegistry.INSTANCE.register(item, renderer::renderByItem);
    }
}
