package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

@Getter
public class ButtonEntry extends AbstractComponentEntry {
    public static final MapCodec<ButtonEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            ComponentSerialization.CODEC.optionalFieldOf("text", Component.empty()).forGetter(ButtonEntry::getText)
    ).apply(instance, ButtonEntry::new));

    protected final Component text;

    protected ButtonEntry(ButtonEntry entry) {
        this(entry, entry.text);
    }

    protected ButtonEntry(AbstractComponentEntry entry, Component text) {
        super(entry);
        this.text = text;
    }
}
