package games.moegirl.sinocraft.sinocore.api.gui.layout;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point {
    public static final MapCodec<Point> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(Point::getX),
            Codec.INT.fieldOf("y").forGetter(Point::getY)
    ).apply(instance, Point::new));

    int x;
    int y;
}
