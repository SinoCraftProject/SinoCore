package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Getter
public final class TextureEntry extends AbstractWidgetEntry {

    public static final MapCodec<TextureEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.listOf().fieldOf("uv").forGetter(e -> List.of(e.getTextureX(), e.getTextureY())),
            Codec.INT.listOf().fieldOf("size").forGetter(e -> List.of(e.getWidth(), e.getHeight())),
            Codec.INT.listOf().optionalFieldOf("uv_size", null).forGetter(TextureEntry::uvSize),
            Codec.INT.listOf().optionalFieldOf("position", null).forGetter(TextureEntry::position)
    ).apply(instance, TextureEntry::new));

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int textureX;
    private final int textureY;
    private final int textureWidth;
    private final int textureHeight;

    TextureEntry(List<Integer> uv, List<Integer> size, @Nullable List<Integer> uvSize, @Nullable List<Integer> position) {
        this.x = position != null ? position.get(0) : uv.get(0);
        this.y = position != null ? position.get(1) : uv.get(1);
        this.width = size.get(0);
        this.height = size.get(1);
        this.textureX = uv.get(0);
        this.textureY = uv.get(1);
        this.textureWidth = uvSize != null ? uvSize.get(0) : size.get(0);
        this.textureHeight = uvSize != null ? uvSize.get(1) : size.get(1);
    }

    // [u0, u1, v0, v1]
    public float[] normalized(float width, float height) {
        return new float[]{(float) textureX / width, (float) (textureX + textureWidth) / width, (float) getTextureY() / height, (float) (textureY + textureHeight) / height};
    }


    private @Nullable List<Integer> uvSize() {
        return textureWidth == width && textureHeight == height ? null : List.of(textureWidth, textureHeight);
    }

    private @Nullable List<Integer> position() {
        return textureX == x && textureY == y ? null : List.of(x, y);
    }
}
