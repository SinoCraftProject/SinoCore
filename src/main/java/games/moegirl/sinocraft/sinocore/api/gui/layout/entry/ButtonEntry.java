package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ButtonEntry extends AbstractWidgetEntry {
    public static final MapCodec<ButtonEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    AbstractWidgetEntry.MAP_CODEC.forGetter(e -> e),
                    Codec.STRING.fieldOf("text").forGetter(ButtonEntry::getText),
                    Codec.STRING.optionalFieldOf("tooltip", null).forGetter(e -> e.tooltip),
                    Codec.STRING.optionalFieldOf("narration", null).forGetter(e -> e.narration))
            .apply(instance, ButtonEntry::new));

    @Getter
    protected final String text;

    @Nullable
    protected final String tooltip;

    @Nullable
    protected final String narration;

    protected ButtonEntry(ButtonEntry entry) {
        this(entry, entry.text, entry.tooltip, entry.narration);
    }

    protected ButtonEntry(AbstractWidgetEntry entry, String text, @Nullable String tooltip, @Nullable String narration) {
        super(entry);
        this.text = text;
        this.tooltip = tooltip;
        this.narration = narration;
    }

    public Optional<String> getTooltip() {
        return Optional.ofNullable(tooltip);
    }

    public Optional<String> getNarration() {
        return Optional.ofNullable(narration);
    }
}
