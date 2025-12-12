package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Getter
public class GuiTexture extends GuiSprite {
    public static final Size DEFAULT_TEXTURE_SIZE = new Size(256, 256);

    private static final Codec<Size> TEXTURE_SIZE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("textureWidth").forGetter(Size::width),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("textureHeight").forGetter(Size::height)
    ).apply(instance, Size::new));

    private static final MapCodec<Size> SIZE_MAP_CODEC = CodecHelper.withAlternative(
            MapCodec.assumeMapUnsafe(TEXTURE_SIZE_CODEC),
            Size.CODEC.optionalFieldOf("size", null)
    );

    private static final Codec<GuiTexture> CODEC_WITH_UV = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("path").forGetter(GuiSprite::getPath),
            SIZE_MAP_CODEC.fieldOf("size").forGetter(e -> e.size),
            MapCodec.assumeMapUnsafe(UV.CODEC).forGetter(e -> e.uv)
    ).apply(instance, GuiTexture::new));
    private static final Codec<GuiTexture> CODEC_PATH = ResourceLocation.CODEC.comapFlatMap(r -> DataResult.success(new GuiTexture(r)), GuiSprite::getPath);
    private static final Codec<GuiTexture> CODEC_WITH_SIZE = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("path").forGetter(GuiSprite::getPath),
            SIZE_MAP_CODEC.fieldOf("size").forGetter(e -> e.size)
    ).apply(instance, GuiTexture::new));

    public static final Codec<GuiTexture> CODEC = CodecHelper.withDecodingFallbacks(CODEC_WITH_UV, List.of(CODEC_WITH_SIZE, CODEC_PATH));
    public static final MapCodec<GuiTexture> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    @Nullable
    final Size size;
    @Nullable
    final UV uv;

    public GuiTexture(ResourceLocation path) {
        this(path, null);
    }

    public GuiTexture(ResourceLocation path, @Nullable Size size) {
        this(path, null, null);
    }

    public GuiTexture(ResourceLocation path, @Nullable Size size, @Nullable UV uv) {
        super(path);
        this.size = size;
        this.uv = uv;
    }

    public Optional<Size> getSize() {
        return Optional.ofNullable(size);
    }

    public Optional<UV> getUV() {
        return Optional.ofNullable(uv);
    }

    public float getU0() {
        if (size == null || uv == null) {
            return 0;
        }
        return ((float) uv.getUStart()) / size.width();
    }

    public float getV0() {
        if (size == null || uv == null) {
            return 0;
        }
        return ((float) uv.getVStart()) / size.height();
    }

    public float getU1() {
        if (size == null || uv == null) {
            return 1;
        }
        return ((float) (uv.getUEnd())) / size.width();
    }

    public float getV1() {
        if (size == null || uv == null) {
            return 1;
        }
        return ((float) (uv.getVEnd())) / size.height();
    }
}
