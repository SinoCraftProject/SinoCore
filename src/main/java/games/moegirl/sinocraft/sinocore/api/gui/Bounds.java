package games.moegirl.sinocraft.sinocore.api.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class Bounds {
    // Codec with x, y, width, height fields in a same level.
    private static final Codec<Bounds> CODEC_NAMED_XYWH = RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(Position.CODEC).forGetter(Bounds::getOrigin),
            MapCodec.assumeMapUnsafe(Size.CODEC).forGetter(Bounds::getSize)
    ).apply(instance, Bounds::new));

    private static final Codec<Bounds> CODEC_NAMED_XY = RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(Position.CODEC).forGetter(Bounds::getOrigin)
    ).apply(instance, Bounds::new));

    private static final Codec<Bounds> CODEC_LIST = Codec.INT.listOf().comapFlatMap(
            l -> {
                if (l.size() == 4) {
                    return DataResult.success(new Bounds(l.get(0), l.get(1), l.get(2), l.get(3)));
                } else if (l.size() == 2) {
                    return DataResult.success(new Bounds(l.get(0), l.get(1)));
                } else {
                    return DataResult.error(() -> "Expected list of 4 or 2 integers for Bounds, got " + l.size());
                }
            },
            b -> List.of(b.getX(), b.getY(), b.getWidth(), b.getHeight())
    );

    public static final Codec<Bounds> CODEC = CodecHelper.withDecodingFallbacks(CODEC_NAMED_XYWH, List.of(CODEC_NAMED_XY, CODEC_LIST));
    private final Position origin;
    private final Size size;

    public Bounds(Position origin, Size size) {
        this.origin = origin;
        this.size = size;
    }

    public Bounds(int x, int y) {
        this(new Position(x, y));
    }

    public Bounds(Position origin) {
        this(origin, Size.ZERO);
    }

    public Bounds(int x, int y, int width, int height) {
        this(new Position(x, y), new Size(width, height));
    }

    public int getX() {
        return getOrigin().getX();
    }

    public int getY() {
        return getOrigin().getY();
    }

    public int getWidth() {
        return getSize().getWidth();
    }

    public int getHeight() {
        return getSize().getHeight();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Bounds other) {
            return this.origin.equals(other.origin) && this.size.equals(other.size);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin.hashCode(), size.hashCode());
    }

    @Override
    public String toString() {
        return "Bounds[" +
                "origin=" + origin + ", " +
                "size=" + size + ']';
    }
}
