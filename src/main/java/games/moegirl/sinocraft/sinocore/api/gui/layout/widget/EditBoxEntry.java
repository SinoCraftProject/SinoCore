package games.moegirl.sinocraft.sinocore.api.gui.layout.widget;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.jetbrains.annotations.Nullable;

/**
 * @author luqin2007
 */
@Getter
public class EditBoxEntry extends AbstractWidgetEntry {
    public static final int DEFAULT_TEXT_COLOR = 0xE0E0E0;
    public static final int DEFAULT_UNEDITABLE_TEXT_COLOR = 0x707070;

    public static final MapCodec<EditBoxEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractWidgetEntry.MAP_CODEC.forGetter(e -> e),
            ComponentSerialization.CODEC.optionalFieldOf("title", null).forGetter(EditBoxEntry::getTitle),
            ComponentSerialization.CODEC.optionalFieldOf("hint", null).forGetter(EditBoxEntry::getHint),
            Codec.STRING.optionalFieldOf("suggestion", null).forGetter(EditBoxEntry::getSuggestion),
            CodecHelper.unwarpOptional(
                    CodecHelper.optionalAliasedFieldOf(Codec.STRING, "placeholder", "default")
            ).forGetter(EditBoxEntry::getPlaceholder),
            Codec.INT.optionalFieldOf("max_length", Integer.MAX_VALUE).forGetter(EditBoxEntry::getMaxLength),
            CodecHelper.unwarpOptional(
                    CodecHelper.optionalAliasedFieldOf(Codec.INT, "text_color", "color"), DEFAULT_TEXT_COLOR
            ).forGetter(EditBoxEntry::getColor),
            CodecHelper.unwarpOptional(
                    CodecHelper.optionalAliasedFieldOf(Codec.INT, "text_color_uneditable", "color_uneditable"), DEFAULT_UNEDITABLE_TEXT_COLOR
            ).forGetter(EditBoxEntry::getUneditableColor),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("alpha", 1.0F).forGetter(EditBoxEntry::getAlpha),
            Codec.BOOL.optionalFieldOf("bordered", true).forGetter(EditBoxEntry::isBordered)
    ).apply(instance, EditBoxEntry::new));

    @Nullable
    protected final Component title;
    @Nullable
    protected final Component hint;
    @Nullable
    protected final String suggestion;
    protected final String placeholder;
    protected final int maxLength;
    protected final int color;
    protected final int uneditableColor;
    protected final float alpha;
    protected final boolean bordered;

    EditBoxEntry(AbstractWidgetEntry entry, @Nullable Component title, @Nullable Component hint,
                 @Nullable String suggestion, String placeholder, int maxLength,
                 int color, int uneditableColor, float alpha, boolean bordered) {
        super(entry);
        this.title = title;
        this.hint = hint;
        this.maxLength = maxLength;
        this.suggestion = suggestion;
        this.placeholder = placeholder;
        this.color = color;
        this.uneditableColor = uneditableColor;
        this.alpha = alpha;
        this.bordered = bordered;
    }
}
