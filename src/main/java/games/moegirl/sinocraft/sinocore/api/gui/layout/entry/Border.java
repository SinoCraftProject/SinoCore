package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;

import java.util.List;

public record Border(int top, int right, int bottom, int left) {
    private static final Codec<Border> CODEC_NAMED = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("top", 0).forGetter(Border::top),
            Codec.INT.optionalFieldOf("right", 0).forGetter(Border::right),
            Codec.INT.optionalFieldOf("bottom", 0).forGetter(Border::bottom),
            Codec.INT.optionalFieldOf("left", 0).forGetter(Border::left)
    ).apply(instance, Border::new));
    private static final Codec<Border> CODEC_4_LIST = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.listOf().fieldOf("border").forGetter(b -> List.of(b.top(), b.left(), b.bottom(), b.right()))
    ).apply(instance, l -> new Border(l.get(0), l.get(1), l.get(2), l.get(3))));
    private static final Codec<Border> CODEC_2_LIST = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.listOf().fieldOf("border").forGetter(b -> List.of(b.top(), b.left()))
    ).apply(instance, l -> new Border(l.get(0), l.get(1))));
    private static final Codec<Border> CODEC_INT = Codec.INT.comapFlatMap(i -> DataResult.success(new Border(i)), b -> b.top);

    public static final Codec<Border> CODEC = CodecHelper.withDecodingFallbacks(CODEC_NAMED, List.of(CODEC_4_LIST, CODEC_2_LIST, CODEC_INT));

    public Border(int value) {
        this(value, value);
    }

    public Border(int upDown, int leftRight) {
        this(upDown, leftRight, upDown, leftRight);
    }
}
