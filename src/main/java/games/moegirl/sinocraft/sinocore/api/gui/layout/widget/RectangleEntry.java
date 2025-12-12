package games.moegirl.sinocraft.sinocore.api.gui.layout.widget;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;

@Getter
public class RectangleEntry extends AbstractWidgetEntry {

    public static final int DEFAULT_COLOR = 0xFF66CCFF;

    public static final MapCodec<RectangleEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractWidgetEntry.MAP_CODEC.forGetter(e -> e),
            Codec.INT.optionalFieldOf("color", DEFAULT_COLOR).forGetter(RectangleEntry::getColor),
            Codec.INT.optionalFieldOf("border_width", 0).forGetter(RectangleEntry::getBorderWidth),
            Codec.INT.optionalFieldOf("border_color", 0).forGetter(RectangleEntry::getBorderWidth)
    ).apply(instance, RectangleEntry::new));

    protected final int color;

    protected final int borderWidth;
    protected final int borderColor;

    protected RectangleEntry(AbstractWidgetEntry entry) {
        this(entry, DEFAULT_COLOR, 0, 0);
    }

    protected RectangleEntry(AbstractWidgetEntry entry, int color) {
        this(entry, color, 0, 0);
    }

    protected RectangleEntry(AbstractWidgetEntry entry, int color, int borderWidth, int borderColor) {
        super(entry);
        this.color = color;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    public boolean hasBorder() {
        return borderWidth > 0;
    }
}
