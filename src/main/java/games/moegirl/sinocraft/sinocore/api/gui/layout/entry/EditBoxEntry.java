package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author luqin2007
 */
@Getter
public final class EditBoxEntry extends AbstractWidgetEntry {
    public static final MapCodec<EditBoxEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.listOf().fieldOf("position").forGetter(b -> List.of(b.getX(), b.getY())),
                    Codec.INT.listOf().fieldOf("size").forGetter(b -> List.of(b.getWidth(), b.getHeight())),
                    Codec.STRING.optionalFieldOf("title", "").forGetter(EditBoxEntry::getTitle),
                    Codec.STRING.optionalFieldOf("hint", null).forGetter(EditBoxEntry::getHint),
                    Codec.INT.optionalFieldOf("max_length", 32).forGetter(EditBoxEntry::getMaxLength),
                    Codec.STRING.optionalFieldOf("suggestion", null).forGetter(EditBoxEntry::getSuggestion),
                    Codec.STRING.optionalFieldOf("default", "").forGetter(EditBoxEntry::getDefaultValue),
                    Codec.INT.optionalFieldOf("color", 0xE0E0E0).forGetter(EditBoxEntry::getColor),
                    Codec.INT.optionalFieldOf("color_uneditable", 0xE0E0E0).forGetter(EditBoxEntry::getUneditableColor),
                    Codec.floatRange(0.0F, 1.0F).optionalFieldOf("alpha", 1.0F).forGetter(EditBoxEntry::getAlpha),
                    Codec.STRING.optionalFieldOf("tooltip", null).forGetter(EditBoxEntry::getTooltip),
                    Codec.BOOL.optionalFieldOf("bordered", true).forGetter(EditBoxEntry::isBordered))
            .apply(instance, EditBoxEntry::new));

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String title;
    @Nullable
    private final String hint;
    private final int maxLength;
    @Nullable
    private final String suggestion;
    private final String defaultValue;
    private final int color;
    private final int uneditableColor;
    private final float alpha;
    @Nullable
    private final String tooltip;
    private final boolean bordered;

    EditBoxEntry(List<Integer> position, List<Integer> size, String title,
                 @Nullable String hint, int maxLength, @Nullable String suggestion, String defaultValue, int color,
                 int uneditableColor, float alpha, @Nullable String tooltip, boolean bordered) {
        this.x = position.get(0);
        this.y = position.get(1);
        this.width = size.get(0);
        this.height = size.get(1);
        this.title = title;
        this.hint = hint;
        this.maxLength = maxLength;
        this.suggestion = suggestion;
        this.defaultValue = defaultValue;
        this.color = color;
        this.uneditableColor = uneditableColor;
        this.alpha = alpha;
        this.tooltip = tooltip;
        this.bordered = bordered;
    }
}
