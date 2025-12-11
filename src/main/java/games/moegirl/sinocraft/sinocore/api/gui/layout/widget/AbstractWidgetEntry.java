package games.moegirl.sinocraft.sinocore.api.gui.layout.widget;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.Position;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.Size;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents a widget entry in layout JSON.
 */
@Getter
public class AbstractWidgetEntry {
    public static final Codec<AbstractWidgetEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(Bounds.CODEC).forGetter(AbstractWidgetEntry::getBounds),
            Codec.BOOL.optionalFieldOf("enabled", true).forGetter(AbstractWidgetEntry::isEnabled),
            Codec.BOOL.optionalFieldOf("visible", true).forGetter(AbstractWidgetEntry::isVisible),
            ComponentSerialization.CODEC.optionalFieldOf("tooltip", null).forGetter(e -> e.tooltip),
            ComponentSerialization.CODEC.optionalFieldOf("narration", null).forGetter(e -> e.narration)
    ).apply(instance, AbstractWidgetEntry::new));

    public static final MapCodec<AbstractWidgetEntry> MAP_CODEC = MapCodec.assumeMapUnsafe(AbstractWidgetEntry.CODEC);

    protected final Bounds bounds;
    protected final boolean enabled;
    protected final boolean visible;

    @Nullable
    protected final Component tooltip;

    @Nullable
    protected final Component narration;

    protected AbstractWidgetEntry(AbstractWidgetEntry entry) {
        this(entry.bounds, entry.enabled, entry.visible, entry.tooltip, entry.narration);
    }

    protected AbstractWidgetEntry(Bounds bounds) {
        this(bounds, true, true);
    }

    protected AbstractWidgetEntry(Bounds bounds, boolean enabled, boolean visible) {
        this(bounds, enabled, visible, null, null);
    }

    protected AbstractWidgetEntry(Bounds bounds, boolean enabled, boolean visible, @Nullable Component tooltip, @Nullable Component narration) {
        this.bounds = bounds;
        this.enabled = enabled;
        this.visible = visible;
        this.tooltip = tooltip;
        this.narration = narration;
    }

    public Position getPosition() {
        return bounds.origin();
    }

    public int getX() {
        return bounds.origin().x();
    }

    public int getY() {
        return bounds.origin().y();
    }

    public Size getSize() {
        return bounds.size();
    }

    public int getWidth() {
        return bounds.size().width();
    }

    public int getHeight() {
        return bounds.size().height();
    }

    public Optional<Component> getTooltip() {
        return Optional.ofNullable(tooltip);
    }

    public Optional<Component> getNarration() {
        return Optional.ofNullable(narration);
    }
}
