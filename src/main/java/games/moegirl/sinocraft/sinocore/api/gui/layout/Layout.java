package games.moegirl.sinocraft.sinocore.api.gui.layout;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.AbstractComponentEntry;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.GuiSprite;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public class Layout {
    public static final Codec<Layout> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("width").forGetter(Layout::getWidth),
            Codec.INT.fieldOf("height").forGetter(Layout::getHeight),
            Codec.compoundList(Codec.STRING, LayoutLoader.COMPONENTS_CODEC).fieldOf("components").forGetter(Layout::getComponents),
            CodecHelper.unwarpOptional(GuiSprite.CODEC.optionalFieldOf("background")).forGetter(l -> l.background),
            ComponentSerialization.CODEC.optionalFieldOf("title", Component.empty()).forGetter(Layout::getTitle)
    ).apply(instance, Layout::new));

    protected final int width;
    protected final int height;
    protected final Map<String, AbstractComponentEntry> components = new HashMap<>();
    @Nullable
    protected final GuiSprite background;
    protected final Component title;

    Layout(int width, int height, List<Pair<String, AbstractComponentEntry>> components, @Nullable GuiSprite background, Component title) {
        this.width = width;
        this.height = height;
        for (Pair<String, AbstractComponentEntry> nameWidgetPair : components) {
            var name = nameWidgetPair.getFirst();
            var widget = nameWidgetPair.getSecond();
            this.components.put(name, widget);
        }
        this.background = background;
        this.title = title;
    }

    public AbstractComponentEntry getComponent(String name) {
        return Objects.requireNonNull(components.get(name));
    }

    public boolean hasComponent(String name) {
        return components.containsKey(name);
    }

    private List<Pair<String, AbstractComponentEntry>> getComponents() {
        return components.entrySet()
                .stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .toList();
    }

    public Optional<GuiSprite> getBackground() {
        return Optional.ofNullable(background);
    }
}
