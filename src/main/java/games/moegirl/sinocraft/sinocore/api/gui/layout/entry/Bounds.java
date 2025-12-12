package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;

import java.util.List;

public record Bounds(Position origin, Size size) {
    private static final Codec<Bounds> CODEC_LIST_4 = Codec.INT.listOf().comapFlatMap(
            l -> DataResult.success(new Bounds(l.get(0), l.get(1), l.get(2), l.get(3))),
            b -> List.of(b.getX(), b.getY(), b.getWidth(), b.getHeight())
    );
    // Codec with x, y, width, height fields in a same level.
    private static final Codec<Bounds> CODEC_FLATTED = RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(Position.CODEC).forGetter(Bounds::origin),
            MapCodec.assumeMapUnsafe(Size.CODEC).forGetter(Bounds::size)
    ).apply(instance, Bounds::new));
    private static final Codec<Bounds> CODEC_NO_SIZE = RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(Position.CODEC).forGetter(Bounds::origin)
    ).apply(instance, Bounds::new));

    public static final Codec<Bounds> CODEC = CodecHelper.withDecodingFallbacks(CODEC_FLATTED, List.of(CODEC_NO_SIZE, CODEC_LIST_4));

    public Bounds(Position origin) {
        this(origin, Size.ZERO);
    }

    public Bounds(int x, int y, int width, int height) {
        this(new Position(x, y), new Size(width, height));
    }

    public int getX() {
        return origin.x();
    }

    public int getY() {
        return origin.y();
    }

    public int getWidth() {
        return size.width();
    }

    public int getHeight() {
        return size.height();
    }
}
