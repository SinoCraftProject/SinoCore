package games.moegirl.sinocraft.sinocore.api.gui.layout;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.GuiTexture;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.AbstractComponentEntry;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class Layout {
    public static final Codec<Layout> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("width").forGetter(Layout::getWidth),
            Codec.INT.fieldOf("height").forGetter(Layout::getHeight),
            Codec.unboundedMap(Codec.STRING, LayoutLoader.COMPONENTS_CODEC).fieldOf("components").forGetter(Layout::getComponents),
            CodecHelper.unwarpOptional(GuiTexture.CODEC.optionalFieldOf("background")).forGetter(l -> l.background),
            ComponentSerialization.CODEC.optionalFieldOf("title", Component.empty()).forGetter(Layout::getTitle)
    ).apply(instance, Layout::new));

    protected final int width;
    protected final int height;
    protected final Map<String, AbstractComponentEntry> components = new HashMap<>();
    @Nullable
    protected final GuiTexture background;
    protected final Component title;

    Layout(int width, int height, Map<String, AbstractComponentEntry> components,
           @Nullable GuiTexture background, Component title) {
        this.width = width;
        this.height = height;
        this.components.putAll(components);
        this.background = background;
        this.title = title;
    }

    public AbstractComponentEntry getComponent(String name) {
        return Objects.requireNonNull(components.get(name));
    }

    public boolean hasComponent(String name) {
        return components.containsKey(name);
    }

    public ImmutableMap<String, AbstractComponentEntry> getComponents() {
        return ImmutableMap.copyOf(components);
    }
}
