package games.moegirl.sinocraft.sinocore.api.client.neoforge;

import games.moegirl.sinocraft.sinocore.client.SinoClientNeoForge;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class ClientRegisterImpl {

    public static void registerItemModelPredicate(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function) {
        ItemProperties.register(item.asItem(), id, function);
    }

    public static void registerItemColor(ItemColor color, ItemLike... items) {
        SinoClientNeoForge.registerItemColor(color, items);
    }

    public static void registerBlockColor(BlockColor color, Block... blocks) {
        SinoClientNeoForge.registerBlockColor(color, blocks);
    }

    public static void registerItemCustomRenderer(Item item, BlockEntityWithoutLevelRenderer renderer) {
        SinoClientNeoForge.setItemCustomRenderer(item, renderer);
    }
}
