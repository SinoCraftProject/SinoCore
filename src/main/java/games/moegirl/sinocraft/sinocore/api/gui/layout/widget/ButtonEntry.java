package games.moegirl.sinocraft.sinocore.api.gui.layout.widget;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

@Getter
public class ButtonEntry extends AbstractWidgetEntry {
    public static final MapCodec<ButtonEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractWidgetEntry.MAP_CODEC.forGetter(e -> e),
            ComponentSerialization.CODEC.optionalFieldOf("text", Component.empty()).forGetter(ButtonEntry::getText)
    ).apply(instance, ButtonEntry::new));

    protected final Component text;

    protected ButtonEntry(ButtonEntry entry) {
        this(entry, entry.text);
    }

    protected ButtonEntry(AbstractWidgetEntry entry, Component text) {
        super(entry);
        this.text = text;
    }
}
