package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.Direction2D;
import lombok.Getter;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SlotGridEntry extends SlotGroupEntry {
    private static final Codec<SlotGridEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            Direction2D.CODEC.optionalFieldOf("direction", Direction2D.LEFT_TO_RIGHT).forGetter(SlotGridEntry::getDirection),
            ExtraCodecs.NON_NEGATIVE_INT.listOf().fieldOf("rows").forGetter(SlotGridEntry::getRows),
            Codec.INT.listOf().optionalFieldOf("row_offsets", new ArrayList<>()).forGetter(SlotGridEntry::getRowOffsets),
            Codec.INT.listOf().optionalFieldOf("column_offsets", new ArrayList<>()).forGetter(SlotGridEntry::getColumnOffsets),
            Codec.INT.optionalFieldOf("row_span", 0).forGetter(SlotGridEntry::getRowSpan),
            Codec.INT.optionalFieldOf("column_span", 0).forGetter(SlotGridEntry::getColumnSpan)
    ).apply(instance, SlotGridEntry::new));

    public static final MapCodec<SlotGridEntry> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    protected final Direction2D direction;
    protected final List<Integer> rows;
    protected final List<Integer> columnOffsets;
    protected final List<Integer> rowOffsets;
    protected final int rowSpan;
    protected final int columnSpan;

    protected final List<SlotEntry> slots;

    SlotGridEntry(AbstractComponentEntry entry, Direction2D direction, List<Integer> rows,
                  List<Integer> columnOffsets, List<Integer> rowOffsets, int rowSpan, int columnSpan) {
        super(entry);
        this.direction = direction;
        this.rows = rows;
        this.columnOffsets = columnOffsets;
        this.rowOffsets = rowOffsets;
        this.rowSpan = rowSpan;
        this.columnSpan = columnSpan;

        this.slots = createSlots(this, direction, rows, columnOffsets, rowOffsets, rowSpan, columnSpan);
    }

    private static List<SlotEntry> createSlots(SlotGridEntry entry, Direction2D direction,
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
