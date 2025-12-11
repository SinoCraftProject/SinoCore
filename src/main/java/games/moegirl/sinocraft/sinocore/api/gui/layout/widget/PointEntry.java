package games.moegirl.sinocraft.sinocore.api.gui.layout.widget;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;

@Getter
public class PointEntry extends AbstractWidgetEntry {
    public static final MapCodec<PointEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractWidgetEntry.MAP_CODEC.forGetter(e -> e)
    ).apply(instance, PointEntry::new));

    protected PointEntry(AbstractWidgetEntry entry) {
        super(entry);
    }
}
