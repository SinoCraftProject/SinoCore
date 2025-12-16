package games.moegirl.sinocraft.sinocore.api.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.Objects;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class GuiTexture extends GuiImage {
    public static final int DEFAULT_TEXTURE_SIZE = 256;

    public static final Codec<GuiTexture> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(GuiTexture::getPath),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("textureWidth", 256).forGetter(GuiTexture::getTextureWidth),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("textureHeight", 256).forGetter(GuiTexture::getTextureHeight),
            CodecHelper.unwarpOptional(
                    CodecHelper.optionalAliasedFieldOf(ExtraCodecs.NON_NEGATIVE_INT, "uStart", "uPosition", "uOffset"),
                    0
            ).forGetter(GuiTexture::getUOffset),
            CodecHelper.unwarpOptional(
                    CodecHelper.optionalAliasedFieldOf(ExtraCodecs.NON_NEGATIVE_INT, "vStart", "vPosition", "vOffset"),
                    0
            ).forGetter(GuiTexture::getVOffset),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("uWidth").forGetter(t -> Optional.of(t.getUWidth())),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("vHeight").forGetter(t -> Optional.of(t.getVHeight()))
    ).apply(instance,
            (path, textureWidth, textureHeight, uOffset, vOffset,
             uWidth, vHeight) ->
                    new GuiTexture(path, textureWidth, textureHeight, uOffset, vOffset,
                            uWidth.orElse(textureWidth), vHeight.orElse(textureHeight))
    ));

    public static final MapCodec<GuiTexture> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    protected final ResourceLocation path;
    protected final int textureWidth;
    protected final int textureHeight;
    protected final int uOffset;
    protected final int vOffset;
    protected final int uWidth;
    protected final int vHeight;

    public GuiTexture(ResourceLocation path) {
        this(path, DEFAULT_TEXTURE_SIZE, DEFAULT_TEXTURE_SIZE, 0, 0, DEFAULT_TEXTURE_SIZE, DEFAULT_TEXTURE_SIZE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof GuiTexture other) {
            return this.path.equals(other.path) &&
                    this.textureWidth == other.textureWidth &&
                    this.textureHeight == other.textureHeight &&
                    this.uOffset == other.uOffset &&
                    this.vOffset == other.vOffset &&
                    this.uWidth == other.uWidth &&
                    this.vHeight == other.vHeight;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path.hashCode(), textureWidth, textureHeight, uOffset, vOffset, uWidth, vHeight);
    }

    @Override
    public String toString() {
        return "GuiTexture[" +
                "path=" + path + ", " +
                "textureWidth=" + textureWidth + ", " +
                "textureHeight=" + textureHeight + ", " +
                "uOffset=" + uOffset + ", " +
                "vOffset=" + vOffset + ", " +
                "uWidth=" + uWidth + ", " +
                "vHeight=" + vHeight + ']';
    }

    public float getU0() {
        return (float) uOffset / textureWidth;
    }

    public float getV0() {
        return (float) vOffset / textureHeight;
    }

    public float getU1() {
        return ((float) uOffset + uWidth) / textureWidth;
    }

    public float getV1() {
        return ((float) vOffset + vHeight) / textureHeight;
    }
}
