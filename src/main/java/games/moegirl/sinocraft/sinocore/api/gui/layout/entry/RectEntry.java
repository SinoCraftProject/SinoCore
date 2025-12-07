package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;

import java.util.List;

@Getter
public final class RectEntry extends AbstractWidgetEntry {

    public static final MapCodec<RectEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.listOf().fieldOf("position").forGetter(b -> List.of(b.getX(), b.getY())),
                    Codec.INT.listOf().fieldOf("size").forGetter(b -> List.of(b.getWidth(), b.getHeight())))
            .apply(instance, RectEntry::new));

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private RectEntry(List<Integer> position, List<Integer> size) {
        x = position.get(0);
        y = position.get(1);
        width = size.get(0);
        height = size.get(1);
    }
}
