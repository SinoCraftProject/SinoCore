package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.layout.Point;
import games.moegirl.sinocraft.sinocore.api.gui.layout.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.layout.Size;
import games.moegirl.sinocraft.sinocore.api.gui.widgets.WidgetLoader;
import lombok.Getter;

/**
 * Represents a widget entry in layout JSON.
 */
public class AbstractWidgetEntry {
    public static final MapCodec<AbstractWidgetEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Bounds.MAP_CODEC.forGetter(AbstractWidgetEntry::getBounds),
                    Codec.BOOL.optionalFieldOf("enabled", true).forGetter(AbstractWidgetEntry::isEnabled),
                    Codec.BOOL.optionalFieldOf("visible", true).forGetter(AbstractWidgetEntry::isVisible))
            .apply(instance, AbstractWidgetEntry::new));

    protected final String type;

    @Getter
    protected final Bounds bounds;

    @Getter
    protected final boolean enabled;

    @Getter
    protected final boolean visible;

    protected AbstractWidgetEntry(AbstractWidgetEntry entry) {
        this(entry.bounds, entry.enabled, entry.visible);
    }

    protected AbstractWidgetEntry(int x, int y, int width, int height) {
        this(new Bounds(x, y, width, height), true, true);
    }

    protected AbstractWidgetEntry(int x, int y, int width, int height, boolean enabled, boolean visible) {
        this(new Bounds(x, y, width, height), enabled, visible);
    }

    protected AbstractWidgetEntry(Point position, Size size) {
        this(new Bounds(position, size), true, true);
    }

    protected AbstractWidgetEntry(Point position, Size size, boolean enabled, boolean visible) {
        this(new Bounds(position, size), enabled, visible);
    }

    protected AbstractWidgetEntry(Bounds bounds) {
        this(bounds, true, true);
    }

    protected AbstractWidgetEntry(Bounds bounds, boolean enabled, boolean visible) {
        this.type = WidgetLoader.getTypeName(getClass());
        this.bounds = bounds;
        this.enabled = enabled;
        this.visible = visible;
    }

    public Point getPosition() {
        return bounds.getOrigin();
    }

    public int getX() {
        return bounds.getOrigin().getX();
    }

    public int getY() {
        return bounds.getOrigin().getY();
    }

    public Size getSize() {
        return bounds.getSize();
    }

    public int getWidth() {
        return bounds.getSize().getWidth();
    }

    public int getHeight() {
        return bounds.getSize().getHeight();
    }
}
