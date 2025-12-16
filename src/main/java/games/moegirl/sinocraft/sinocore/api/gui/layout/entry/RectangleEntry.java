package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.Border;
import lombok.Getter;

@Getter
public class RectangleEntry extends AbstractComponentEntry {

    public static final int DEFAULT_COLOR = 0xFF66CCFF;

    public static final MapCodec<RectangleEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            Codec.INT.optionalFieldOf("color", DEFAULT_COLOR).forGetter(RectangleEntry::getColor),
            Border.CODEC.optionalFieldOf("border", Border.ZERO).forGetter(RectangleEntry::getBorder),
            Codec.INT.optionalFieldOf("border_color", 0).forGetter(RectangleEntry::getBorderColor)
    ).apply(instance, RectangleEntry::new));

    protected final int color;

    protected final Border border;
    protected final int borderColor;

    protected RectangleEntry(AbstractComponentEntry entry) {
        this(entry, DEFAULT_COLOR);
    }

    protected RectangleEntry(AbstractComponentEntry entry, int color) {
        this(entry, color, Border.ZERO, 0);
    }

    protected RectangleEntry(AbstractComponentEntry entry, int color, Border border, int borderColor) {
        super(entry);
        this.color = color;
        this.border = border;
        this.borderColor = borderColor;
    }

    public boolean hasBorder() {
        return border.hasBorder();
    }
}
