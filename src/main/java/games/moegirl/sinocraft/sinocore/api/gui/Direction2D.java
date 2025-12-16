package games.moegirl.sinocraft.sinocore.api.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lombok.Getter;

@Getter
public enum Direction2D {
    LEFT_TO_RIGHT(false, false),
    TOP_TO_BOTTOM(true, false),
    RIGHT_TO_LEFT(false, true),
    BOTTOM_TO_TOP(true, true),
    ;

    public static final Codec<Direction2D> CODEC = Codec.STRING.comapFlatMap(Direction2D::parse, Direction2D::toString);

    private final boolean isVertical;
    private final boolean isReversed;

    Direction2D(boolean isVertical, boolean isReversed) {
        this.isVertical = isVertical;
        this.isReversed = isReversed;
    }

    public boolean isHorizontal() {
        return !isVertical();
    }

    public static Direction2D from(String str) {
        return parse(str).getOrThrow();
    }

    public static DataResult<Direction2D> parse(String str) {
        return switch (str) {
            case "horizontal", "left_to_right", "ltr" -> DataResult.success(LEFT_TO_RIGHT);
            case "vertical", "top_to_bottom", "ttb" -> DataResult.success(TOP_TO_BOTTOM);
            case "-horizontal", "right_to_left", "rtl" -> DataResult.success(RIGHT_TO_LEFT);
            case "-vertical", "bottom_to_top", "btt" -> DataResult.success(BOTTOM_TO_TOP);
            default -> DataResult.error(() -> "Unknown direction: " + str);
        };

    }

    public String toString() {
        return switch (this) {
            case LEFT_TO_RIGHT -> "left_to_right";
            case TOP_TO_BOTTOM -> "top_to_bottom";
            case RIGHT_TO_LEFT -> "right_to_left";
            case BOTTOM_TO_TOP -> "bottom_to_top";
        };
    }
}
