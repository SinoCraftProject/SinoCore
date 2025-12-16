package games.moegirl.sinocraft.sinocore.api.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import net.minecraft.util.ExtraCodecs;

import java.util.List;
import java.util.Objects;

@Getter
public class Size {
    private static final Codec<Size> CODEC_NAMED = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("width").forGetter(Size::getWidth),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("height").forGetter(Size::getHeight)
    ).apply(instance, Size::new));

    private static final Codec<Size> CODEC_LIST = ExtraCodecs.NON_NEGATIVE_INT.listOf().comapFlatMap(
            l -> {
                if (l.size() == 2) {
                    return DataResult.success(new Size(l.get(0), l.get(1)));
                } else {
                    return DataResult.error(() -> "Expected a list of 2 non negative integers for Size, got " + l.size());
                }
            },
            s -> List.of(s.getWidth(), s.getHeight())
    );

    public static final Codec<Size> CODEC = CodecHelper.withDecodingFallback(CODEC_NAMED, CODEC_LIST);

    public static final Size ZERO = new Size(0, 0);

    protected final int width;
    protected final int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Size other) {
            return this.width == other.width && this.height == other.height;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return "Size[" +
                "width=" + width + ", " +
                "height=" + height + ']';
    }
}
