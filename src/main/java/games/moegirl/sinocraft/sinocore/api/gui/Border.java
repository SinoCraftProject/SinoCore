package games.moegirl.sinocraft.sinocore.api.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class Border {
    private static final Codec<Border> CODEC_NAMED = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("top", 0).forGetter(Border::getTop),
            Codec.INT.optionalFieldOf("right", 0).forGetter(Border::getRight),
            Codec.INT.optionalFieldOf("bottom", 0).forGetter(Border::getBottom),
            Codec.INT.optionalFieldOf("left", 0).forGetter(Border::getLeft)
    ).apply(instance, Border::new));

    private static final Codec<Border> CODEC_LIST = Codec.INT.listOf().comapFlatMap(l -> {
        if (l.size() == 4) {
            return DataResult.success(new Border(l.get(0), l.get(1), l.get(2), l.get(3)));
        } else if (l.size() == 2) {
            return DataResult.success(new Border(l.get(0), l.get(1)));
        } else {
            return DataResult.error(() -> "Expected list of 4 or 2 integers for Border, got " + l.size());
        }
    }, b -> List.of(b.getTop(), b.getRight(), b.getBottom(), b.getLeft()));

    private static final Codec<Border> CODEC_INT = Codec.INT.comapFlatMap(i -> DataResult.success(new Border(i)), Border::getTop);

    public static final Codec<Border> CODEC = CodecHelper.withDecodingFallbacks(CODEC_NAMED, List.of(CODEC_LIST, CODEC_INT));

    private final int top;
    private final int right;
    private final int bottom;
    private final int left;

    public Border(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public Border(int value) {
        this(value, value);
    }

    public Border(int upDown, int leftRight) {
        this(upDown, leftRight, upDown, leftRight);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Border other) {
            return this.top == other.top &&
                   this.right == other.right &&
                   this.bottom == other.bottom &&
                   this.left == other.left;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(top, right, bottom, left);
    }

    @Override
    public String toString() {
        return "Border[" +
                "top=" + top + ", " +
                "right=" + right + ", " +
                "bottom=" + bottom + ", " +
                "left=" + left + ']';
    }
}
