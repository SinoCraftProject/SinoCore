package games.moegirl.sinocraft.sinocore.api.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class Position {
    private static final Codec<Position> CODEC_NAMED = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(Position::getX),
            Codec.INT.fieldOf("y").forGetter(Position::getY)
    ).apply(instance, Position::new));

    private static final Codec<Position> CODEC_LIST = Codec.INT.listOf().comapFlatMap(l -> {
                if (l.size() == 2) {
                    return DataResult.success(new Position(l.get(0), l.get(1)));
                } else {
                    return DataResult.error(() -> "Expected a list of 2 integers for Position, got " + l.size());
                }
            },
            p -> List.of(p.getX(), p.getY())
    );

    public static final Codec<Position> CODEC = CodecHelper.withDecodingFallback(CODEC_NAMED, CODEC_LIST);

    public static final Position ZERO = new Position(0, 0);

    protected final int x;
    protected final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Position other) {
            return this.x == other.x && this.y == other.y;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position[" +
                "x=" + x + ", " +
                "y=" + y + ']';
    }

    public RelativePosition relativeTo(Position newPos) {
        var x = getX() - newPos.getX();
        var y = getY() - newPos.getY();
        return new RelativePosition(newPos, x, y);
    }

    public Position toAbsolute() {
        return this;
    }

    @Getter
    public static class RelativePosition extends Position {
        private final Position parent;

        public RelativePosition(Position parent, int x, int y) {
            super(x, y);
            this.parent = parent;
        }

        @Override
        public int getX() {
            return parent.x + x;
        }

        @Override
        public int getY() {
            return parent.y + y;
        }

        public int getRelativeX() {
            return x;
        }

        public int getRelativeY() {
            return y;
        }

        public boolean equalsInValue(Position position) {
            return getX() == position.x && getY() == position.y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj instanceof RelativePosition other) {
                return this.parent.equals(other.parent) && this.x == other.x && this.y == other.y;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(parent.hashCode(), x, y);
        }

        @Override
        public String toString() {
            return "RelativePosition[" +
                    "to=" + parent + ", " +
                    "x=" + x + ", " +
                    "y=" + y + ']';
        }

        @Override
        public Position toAbsolute() {
            return new Position(getX(), getY());
        }
    }
}
