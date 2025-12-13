package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class SlotGroupEntry extends AbstractComponentEntry {
    private static final Codec<SlotGroupEntry> SLOT_LIST_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            SlotEntry.CODEC.listOf().fieldOf("slots").forGetter(SlotGroupEntry::getSlots)
    ).apply(instance, SlotGroupEntry::new));

    private static final MapCodec<SlotGroupEntry> SLOT_LIST_MAP_CODEC = MapCodec.assumeMapUnsafe(SLOT_LIST_CODEC);
    private static final MapCodec<SlotGroupEntry> SLOT_GRID_MAP_CODEC = new MapCodec<>() {
        @Override
        public <T> RecordBuilder<T> encode(SlotGroupEntry input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            return SLOT_LIST_MAP_CODEC.encode(input, ops, prefix);
        }

        @Override
        public <T> DataResult<SlotGroupEntry> decode(DynamicOps<T> ops, MapLike<T> input) {
            var widget = AbstractComponentEntry.MAP_CODEC.decode(ops, input).getOrThrow();
            var direction = Direction2D.CODEC.optionalFieldOf("direction", Direction2D.LEFT_TO_RIGHT).decode(ops, input).getOrThrow();
            var rowSizes = ExtraCodecs.NON_NEGATIVE_INT.listOf().fieldOf("rows").decode(ops, input).getOrThrow();
            var rowOffsets = ExtraCodecs.NON_NEGATIVE_INT.listOf().optionalFieldOf("row_offsets", new ArrayList<>()).decode(ops, input).getOrThrow();
            var columnOffsets = ExtraCodecs.NON_NEGATIVE_INT.listOf().optionalFieldOf("column_offsets", new ArrayList<>()).decode(ops, input).getOrThrow();
            var rowSpan = Codec.INT.optionalFieldOf("row_span", 0).decode(ops, input).getOrThrow();
            var columnSpan = Codec.INT.optionalFieldOf("column_span", 0).decode(ops, input).getOrThrow();
            return DataResult.success(new SlotGroupEntry(widget, direction, rowSizes, rowOffsets, columnOffsets, rowSpan, columnSpan));
        }

        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.concat(AbstractComponentEntry.MAP_CODEC.keys(ops), Stream.of(
                    ops.createString("direction"),
                    ops.createString("rows"),
                    ops.createString("row_offsets"),
                    ops.createString("column_offsets"),
                    ops.createString("row_span"),
                    ops.createString("column_span")
            ));
        }
    };

    public static final MapCodec<SlotGroupEntry> MAP_CODEC = CodecHelper.withDecodingFallback(SLOT_LIST_MAP_CODEC, SLOT_GRID_MAP_CODEC);

    protected final List<SlotEntry> slots;

    SlotGroupEntry(AbstractComponentEntry entry, Direction2D direction, List<Integer> rows,
                   List<Integer> columnOffsets, List<Integer> rowOffsets, int rowSpan, int columnSpan) {
        super(entry);
        this.slots = createSlots(this, direction, rows, columnOffsets, rowOffsets, rowSpan, columnSpan);
    }

    SlotGroupEntry(AbstractComponentEntry entry, List<SlotEntry> slots) {
        super(entry);
        this.slots = slots;
    }

    private static List<SlotEntry> createSlots(SlotGroupEntry entry, Direction2D direction,
                                               List<Integer> rows,
                                               List<Integer> columnOffsets, List<Integer> rowOffsets,
                                               int rowSpan, int columnSpan) {
        var slotSize = 18;
        var result = new ArrayList<SlotEntry>();
        var rowDirection = direction.isHorizontal() ? 1 : -1;
        var columnDirection = !direction.isReversed() ? 1 : -1;
        var x = entry.getX() + (direction.isHorizontal() ? 0 : entry.getWidth());
        var y = entry.getY() + (!direction.isReversed() ? 0 : entry.getHeight());
        for (var i = 0; i < rows.size(); i++) {
            for (var j = 0; j < rows.get(i); j++) {
                var xOffset = j < columnOffsets.size() ? columnOffsets.get(j) : 0;
                var yOffset = i < rowOffsets.size() ? rowOffsets.get(i) : 0;
                result.add(new SlotEntry(new AbstractComponentEntry(new Bounds(x + xOffset, y + yOffset, slotSize, slotSize))));
                x += (slotSize + columnSpan) * rowDirection;
            }
            y += (slotSize + rowSpan) * columnDirection;
            x = entry.getX();
        }
        return result;
    }

    public SlotEntry getSlot(int index) {
        return slots.get(index);
    }

    public int getSlotCount() {
        return slots.size();
    }
}
