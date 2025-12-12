package games.moegirl.sinocraft.sinocore.api.gui.layout.widget;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.Direction2D;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.Position;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class SlotGroupEntry extends AbstractWidgetEntry {
    public static final Codec<SlotGroupEntry> SLOT_LIST_CODEC = SlotEntry.CODEC.listOf().comapFlatMap(list -> lis);

    public static final MapCodec<SlotGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("direction", "horizontal").forGetter(SlotGroupEntry::getDirection),
                    Codec.INT.optionalFieldOf("size", 18).forGetter(SlotGroupEntry::getSize),
                    Codec.either(Codec.INT, Codec.INT.listOf())
                            .optionalFieldOf("count", Either.left(1))
                            .forGetter(e -> Either.right(Arrays.stream(e.getCounts()).boxed().toList())),
                    Codec.INT.listOf().fieldOf("position").forGetter(e -> List.of(e.getX(), e.getY())),
                    Codec.either(Codec.INT, Codec.INT.listOf())
                            .optionalFieldOf("offset", Either.left(0))
                            .forGetter(e -> Either.right(Arrays.stream(e.getOffsets()).boxed().toList())))
            .apply(instance, SlotGroupEntry::new));

    protected final boolean isVertical;
    protected final int size, x, y;
    protected final int[] counts;
    protected final int[] offsets;
    protected final List<SlotEntry> slots;

    SlotGroupEntry(AbstractWidgetEntry entry, Direction2D direction, int rowCount, int[] rowSize,
                   int[] columnOffsets, int[] rowOffsets, int rowSpan, int columnSpan) {
        super(entry);

    }

    private static List<SlotEntry> createSlots(SlotGroupEntry entry, Direction2D direction,
                                               int rowCount, int[] rowSize,
                                               int[] columnOffsets, int[] rowOffsets,
                                               int rowSpan, int columnSpan) {
        var slotSize = 18;
        var result = new ArrayList<SlotEntry>();
        var x = entry.getX();
        var y = entry.getY();
        for (var i = 0; i < rowCount; i++) {
            for (var j = 0; j < rowSize[i]; j++) {
                var xOffset = j < columnOffsets.length ? columnOffsets[j] : 0;
                var yOffset = i < rowOffsets.length ? rowOffsets[i] : 0;
                result.add(new SlotEntry(new AbstractWidgetEntry(new Bounds(x + xOffset, y + yOffset, slotSize, slotSize))));
                x += slotSize + columnSpan;
            }
            y += slotSize + rowSpan;
            x = entry.getX();
        }
        return result;
    }

    SlotGroupEntry(String direction, int size, Either<Integer, List<Integer>> count, List<Integer> position,
                   Either<Integer, List<Integer>> offset) {
        this.isVertical = "vertical".equals(direction);
        this.size = size;
        this.counts = count.map(i -> new int[]{1, i}, list -> list.stream().mapToInt(i -> i).toArray());
        this.x = position.get(0);
        this.y = position.get(1);
        this.offsets = offset.map(i -> new int[]{0, i}, list -> list.stream().mapToInt(i -> i).toArray());

        ImmutableList.Builder<SlotEntry> slots = ImmutableList.builder();
        for (int i = 0; i < counts[0]; i++) {
            for (int j = 0; j < counts[1]; j++) {
                if (isVertical) {
                    slots.add(new SlotEntry(size, List.of(x + i * size + i * offsets[0], y + j * size + j * offsets[1])));
                } else {
                    slots.add(new SlotEntry(size, List.of(x + j * size + j * offsets[0], y + i * size + i * offsets[1])));
                }
            }
        }
        this.slots = slots.build();
    }

    public SlotEntry getSlot(int index) {
        return slots.get(index);
    }

    public int getSlotCount() {
        return slots.size();
    }

    private String getDirection() {
        return isVertical ? "vertical" : "horizontal";
    }
}
