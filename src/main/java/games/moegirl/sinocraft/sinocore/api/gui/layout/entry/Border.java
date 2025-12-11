package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record Border(int top, int left, int bottom, int right) {
    public static final Codec<Border> CODEC_NAMED = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("top").forGetter(Border::top),
            Codec.INT.fieldOf("left").forGetter(Border::left),
            Codec.INT.fieldOf("bottom").forGetter(Border::bottom),
            Codec.INT.fieldOf("right").forGetter(Border::right)
    ).apply(instance, Border::new));

    public static final Codec<Border> CODEC_4_LIST = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.listOf().fieldOf("border").forGetter(b -> List.of(b.top(), b.left(), b.bottom(), b.right()))
    ).apply(instance, l -> new Border(l.get(0), l.get(1), l.get(2), l.get(3))));

    public static final Codec<Border> CODEC_2_LIST = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.listOf().fieldOf("border").forGetter(b -> List.of(b.top(), b.left()))
    ).apply(instance, l -> new Border(l.get(0), l.get(1))));

    public static final Codec<Border> CODEC = Codec.withAlternative(CODEC_NAMED, Codec.withAlternative(CODEC_4_LIST, CODEC_2_LIST));

    public Border(int upDown, int leftRight) {
        this(upDown, leftRight, upDown, leftRight);
    }
}
