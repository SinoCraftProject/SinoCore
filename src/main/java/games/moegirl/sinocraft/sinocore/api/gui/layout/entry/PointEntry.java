package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;

import java.util.List;

@Getter
public final class PointEntry extends AbstractWidgetEntry {
    public static final MapCodec<PointEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.listOf().fieldOf("position").forGetter(b -> List.of(b.getX(), b.getY())))
            .apply(instance, PointEntry::new));

    private final int x;
    private final int y;

    PointEntry(List<Integer> xy) {
        this.x = xy.get(0);
        this.y = xy.get(1);
    }
}
