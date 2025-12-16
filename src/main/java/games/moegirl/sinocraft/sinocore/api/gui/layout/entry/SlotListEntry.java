package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;

import java.util.List;

@Getter
public class SlotListEntry extends SlotGroupEntry {
    public static final Codec<SlotListEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            SlotEntry.CODEC.listOf().fieldOf("slots").forGetter(SlotListEntry::getSlots)
    ).apply(instance, SlotListEntry::new));

    public static final MapCodec<SlotListEntry> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    protected final List<SlotEntry> slots;

    SlotListEntry(AbstractComponentEntry entry, List<SlotEntry> slots) {
        super(entry);
        this.slots = slots;
    }
}
