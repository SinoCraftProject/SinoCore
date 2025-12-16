package games.moegirl.sinocraft.sinocore.api.client.gui.layout;

import games.moegirl.sinocraft.sinocore.api.client.gui.GuiImageRender;
import games.moegirl.sinocraft.sinocore.api.client.gui.component.*;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.client.gui.component.window.AbstractWindow;
import games.moegirl.sinocraft.sinocore.api.gui.layout.Layout;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.*;
import games.moegirl.sinocraft.sinocore.api.client.gui.screen.IScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

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

            if (entry instanceof ImageButtonEntry imageButton) {
                addComponent(name, createImageButton(imageButton, button -> {}));
            } else if (entry instanceof ButtonEntry button) {
                addComponent(name, createButton(button, b -> {}));
            } else if (entry instanceof EditBoxEntry editBox) {
                addComponent(name, createEditBox(editBox));
            } else if (entry instanceof ProgressBarEntry progressBar) {
                addComponent(name, createProgressBar(progressBar, () -> 0.5));
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (layout.getBackground() != null) {
            GuiImageRender.blitImage(layout.getBackground(), guiGraphics, getX(), getY(), getWidth(), getHeight());
        }

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

    // region Create components

    private Bounds getComponentBounds(AbstractComponentEntry entry) {
        return new Bounds(getX() + entry.getX(), getY() + entry.getY(), entry.getWidth(), entry.getHeight());
    }

    protected ButtonComponent createButton(ButtonEntry entry, Button.OnPress onPress) {
        return new ButtonComponent(getComponentBounds(entry), entry.getText(), onPress);
    }

    protected ButtonComponent createImageButton(ImageButtonEntry entry, Button.OnPress onPress) {
        return new ImageButtonComponent(getComponentBounds(entry),
                entry.getTexture(), entry.getTextureHover(), entry.getTexturePressed(), entry.getTextureDisabled(),
                onPress);
    }

    protected EditBoxComponent createEditBox(EditBoxEntry entry, Font font) {
        return new EditBoxComponent(font, entry.getBounds(), entry.getTitle(), entry.getHint(),
                entry.getHint(), entry.getSuggestion(), entry.getPlaceholder(),
                entry.getMaxLength(), entry.getColor(), entry.getUneditableColor(),
                entry.isBordered());
    }

    protected EditBoxComponent createEditBox(EditBoxEntry entry) {
        return createEditBox(entry, getMinecraft().font);
    }

    protected ProgressBarComponent createProgressBar(ProgressBarEntry entry, DoubleSupplier progress) {
        var progressBar = new ProgressBarComponent(entry.getBounds(), entry.getDirection(),
                entry.getTexture(), entry.getTextureFilled(), progress);
        addChild(progressBar);
        return progressBar;
    }

    // endregion
}
