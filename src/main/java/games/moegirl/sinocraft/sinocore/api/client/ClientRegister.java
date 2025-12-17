package games.moegirl.sinocraft.sinocore.api.client;

import games.moegirl.sinocraft.sinocore.client.platform.ClientPlatform;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class ClientRegister {

    public static void registerItemModelPredicate(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function) {
        ClientPlatform.registerItemModelPredicate(item, id, function);
    }

    public static void registerItemColor(ItemColor color, ItemLike... item) {
        ClientPlatform.registerItemColor(color, item);
    }

    public static void registerBlockColor(BlockColor color, Block... blocks) {
        ClientPlatform.registerBlockColor(color, blocks);
    }

    public static void registerItemCustomRenderer(Item item, BlockEntityWithoutLevelRenderer renderer) {
        ClientPlatform.registerItemCustomRenderer(item, renderer);
    }
}
