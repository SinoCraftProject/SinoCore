package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;

@Getter
public class PointEntry extends AbstractComponentEntry {
    public static final MapCodec<PointEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e)
    ).apply(instance, PointEntry::new));

    protected PointEntry(AbstractComponentEntry entry) {
        super(entry);
    }
}
