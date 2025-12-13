package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

@Getter
public final class TextEntry extends AbstractComponentEntry {
    public static final Codec<TextEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            Codec.INT.optionalFieldOf("color", 0xFFFFFFFF).forGetter(TextEntry::getColor),
            ComponentSerialization.CODEC.optionalFieldOf("text", Component.empty()).forGetter(TextEntry::getText),
            Codec.BOOL.optionalFieldOf("shadowed", false).forGetter(TextEntry::isShadowed),
            Codec.BOOL.optionalFieldOf("centered", false).forGetter(TextEntry::isCentered)
    ).apply(instance, TextEntry::new));

    public static final MapCodec<TextEntry> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    private final int color;
    private final Component text;
    private final boolean shadowed;
    private final boolean centered;

    TextEntry(AbstractComponentEntry entry, int color, Component text, boolean shadowed, boolean centered) {
        super(entry);
        this.color = color;
        this.text = text;
        this.shadowed = shadowed;
        this.centered = centered;
    }
}
