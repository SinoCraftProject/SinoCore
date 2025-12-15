package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.Position;
import games.moegirl.sinocraft.sinocore.api.gui.Size;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents a widget entry in layout JSON.
 */
@Getter
public class AbstractComponentEntry {
    public static final Codec<AbstractComponentEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(Bounds.CODEC).forGetter(AbstractComponentEntry::getBounds),
            Codec.BOOL.optionalFieldOf("enabled", true).forGetter(AbstractComponentEntry::isEnabled),
            Codec.BOOL.optionalFieldOf("visible", true).forGetter(AbstractComponentEntry::isVisible),
            ComponentSerialization.CODEC.optionalFieldOf("tooltip", null).forGetter(e -> e.tooltip),
            ComponentSerialization.CODEC.optionalFieldOf("narration", null).forGetter(e -> e.narration)
    ).apply(instance, AbstractComponentEntry::new));

    public static final MapCodec<AbstractComponentEntry> MAP_CODEC = MapCodec.assumeMapUnsafe(AbstractComponentEntry.CODEC);

    protected final Bounds bounds;
    protected final boolean enabled;
    protected final boolean visible;

    @Nullable
    protected final Component tooltip;

    @Nullable
    protected final Component narration;

    protected AbstractComponentEntry(AbstractComponentEntry entry) {
        this(entry.bounds, entry.enabled, entry.visible, entry.tooltip, entry.narration);
    }

    protected AbstractComponentEntry(Bounds bounds) {
        this(bounds, true, true);
    }

    protected AbstractComponentEntry(Bounds bounds, boolean enabled, boolean visible) {
        this(bounds, enabled, visible, null, null);
    }

    protected AbstractComponentEntry(Bounds bounds, boolean enabled, boolean visible, @Nullable Component tooltip, @Nullable Component narration) {
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
