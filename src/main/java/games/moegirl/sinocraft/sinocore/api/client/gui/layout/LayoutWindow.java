package games.moegirl.sinocraft.sinocore.api.client.gui.layout;

import games.moegirl.sinocraft.sinocore.api.client.gui.GuiImageRender;
import games.moegirl.sinocraft.sinocore.api.client.gui.component.*;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.client.gui.component.window.AbstractWindow;
import games.moegirl.sinocraft.sinocore.api.gui.layout.Layout;
import games.moegirl.sinocraft.sinocore.api.gui.layout.LayoutManager;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.*;
import games.moegirl.sinocraft.sinocore.api.client.gui.screen.IScreen;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class LayoutWindow extends AbstractWindow {
    protected final IScreen screen;
    protected final Layout layout;

    protected final Map<String, IComponent> components = new LinkedHashMap<>();

    public LayoutWindow(IScreen screen, Layout layout) {
        super(getStart(screen.getWidth(), layout.getWidth()), getStart(screen.getHeight(), layout.getHeight()),
                layout.getWidth(), layout.getHeight(), layout.getTitle());
        this.screen = screen;
        this.layout = layout;
        setParent(screen);
    }

    @Override
    public @Nullable IScreen getParent() {
        return screen;
    }

    private static int getStart(int total, int size) {
        return (total - size) / 2;
    }

    @Override
    public void createChildren() {
        // Create all default components.
        for (var e : layout.getComponents().entrySet()) {
            var name = e.getKey();
            var entry = e.getValue();

            if (components.containsKey(name)) {
                continue;
            }

            var component = LayoutClientFactories.createComponent(entry, this);
            if (component != null) {
                addComponent(name, component);
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var background = LayoutManager.getBackground(layout);
        GuiImageRender.blitImage(background, guiGraphics, getX(), getY(), getWidth(), getHeight());

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void addComponent(String name, IComponent component) {
        addChild(component);
        components.put(name, component);
    }

    @SuppressWarnings("unchecked")
    public <T extends IComponent> T getComponent(Class<T> type, String name) {
        var component = components.get(name);
        return (T) component;
    }
}
