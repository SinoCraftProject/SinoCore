package games.moegirl.sinocraft.sinocore.api.gui.layout.widget;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;

import java.util.List;

@Getter
public final class TextEntry extends AbstractWidgetEntry {
    public static final MapCodec<TextEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.listOf().fieldOf("position").forGetter(e -> List.of(e.getX(), e.getY())),
                    Codec.INT.optionalFieldOf("color", 0x808080).forGetter(TextEntry::getColor),
                    Codec.STRING.optionalFieldOf("text", "").forGetter(TextEntry::getText),
                    Codec.STRING.optionalFieldOf("rawText", "").forGetter(TextEntry::getRawText),
                    Codec.BOOL.optionalFieldOf("shadow", false).forGetter(TextEntry::isShadowed),
                    Codec.BOOL.optionalFieldOf("center", false).forGetter(TextEntry::isCentered))
            .apply(instance, TextEntry::new));

    private final int x;
    private final int y;
    private final int color;
    private final String text;
    private final String rawText;
    private final boolean shadowed;
    private final boolean centered;

    TextEntry(List<Integer> position, int color, String text, String rawText,
              boolean shadowed, boolean centered) {
        this.x = position.get(0);
        this.y = position.get(1);
        this.color = color;
        this.text = text;
        this.rawText = rawText;
        this.shadowed = shadowed;
        this.centered = centered;
    }
}
