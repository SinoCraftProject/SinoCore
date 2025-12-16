package games.moegirl.sinocraft.sinocore.api.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public abstract class GuiImage {

    public static final Codec<GuiImage> CODEC = CodecHelper.withDecodingFallback(Type.SPRITE_CODEC, Type.TEXTURE_CODEC);
    public static final MapCodec<GuiImage> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    static class Type {
        static final Codec<GuiImage> TEXTURE_CODEC = GuiTexture.CODEC.flatComapMap(
                Function.identity(),
                i -> {
                    if (i instanceof GuiTexture t) {
                        return DataResult.success(t);
                    }
                    return DataResult.error(() -> i.getClass() + " is not a GuiTexture");
                }
        );

        static final Codec<GuiImage> SPRITE_CODEC = GuiSprite.CODEC.flatComapMap(
                Function.identity(),
                i -> {
                    if (i instanceof GuiSprite s) {
                        return DataResult.success(s);
                    }
                    return DataResult.error(() -> i.getClass() + " is not a GuiSprite");
                }
        );
    }

    public abstract void blit(GuiGraphics guiGraphics, int x, int y, int width, int height);
}
