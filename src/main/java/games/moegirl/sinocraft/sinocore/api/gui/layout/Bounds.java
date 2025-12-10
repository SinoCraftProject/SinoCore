package games.moegirl.sinocraft.sinocore.api.gui.layout;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bounds {
    public static final MapCodec<Bounds> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Point.MAP_CODEC.forGetter(Bounds::getOrigin),
            Size.MAP_CODEC.forGetter(Bounds::getSize)
    ).apply(instance, Bounds::new));

    Point origin;
    Size size;

    public Bounds(int x, int y, int width, int height) {
        this(new Point(x, y), new Size(width, height));
    }
}
