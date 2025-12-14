package games.moegirl.sinocraft.sinocore.api.gui.screen;

import games.moegirl.sinocraft.sinocore.api.gui.component.IComponent;
import games.moegirl.sinocraft.sinocore.api.gui.component.window.IWindow;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractScreen extends Screen implements IScreen {
    protected AbstractScreen(Component title) {
        super(title);
    }

    // region IComponent

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    // endregion

    // region Windows holder

    /**
     * Map<IWindow window, Boolean shown>
     */
    private final Map<IWindow, Boolean> windows = new HashMap<>();

    @Nullable
    private IWindow mutexWindow = null;

    @Nullable
    private IWindow focusedWindow = null;

    @Override
    public List<IWindow> getWindows() {
        return List.copyOf(windows.keySet());
    }

    @Override
    public void addWindow(IWindow window, boolean mutex, boolean show) {
        addChild(window);
        windows.put(window, false);
        window.onOpen();

        if (mutex) {
            if (!hasMutexWindow()) {
                setMutexWindow(window);
            } else {
                throw new IllegalStateException();
            }
        }

        if (show) {
            show(window);
        }
    }

    @Override
    public void closeWindow(IWindow window) {
        if (getMutexWindow() == window) {
            setMutexWindow(null);
        }

        if (getFocusedWindow() == window) {
            setFocusedWindow(null);
        }

        removeChild(window);
        windows.remove(window);
        window.onClose();
    }

    @Override
    public void show(IWindow window) {
        if (!windows.get(window)) {
            windows.put(window, true);
            window.onShown();
        }
    }

    @Override
    public void hide(IWindow window) {
        if (windows.get(window) && getMutexWindow() != window) {
            windows.put(window, false);
            window.onHidden();
        }
    }

    @Override
    public @Nullable IWindow getMutexWindow() {
        return mutexWindow;
    }

    @Override
    public void setMutexWindow(@Nullable IWindow window) {
        if (window != null && hasWindow(window)) {
            setFocusedWindow(window);
            mutexWindow = window;
        } else {
            mutexWindow = null;
        }
    }

    @Override
    public @Nullable IWindow getFocusedWindow() {
        return focusedWindow;
    }

    @Override
    public void setFocusedWindow(@Nullable IWindow window) {
        if (hasMutexWindow()) {
            setFocused(getMutexWindow());
            return;
        }

        setFocused(window);
        for (var w : getWindows()) {
            w.setFocused(w == window);
        }
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        super.setFocused(focused);

        if (focused instanceof IWindow window) {
            this.focusedWindow = window;
        } else {
            this.focusedWindow = null;
        }
    }

    private final List<IComponent> children = new ArrayList<>();

    @Override
    public void addChild(IComponent child) {
        children.add(child);
        child.setParent(this);
        addRenderableWidget(child);
    }

    @Override
    public void removeChild(IComponent child) {
        children.remove(child);
        child.setParent(null);
        removeWidget(child);
    }

    @Override
    public List<IComponent> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.copyOf(getChildren());
    }

    @Override
    protected void rebuildWidgets() {
        deinitialize();
        super.rebuildWidgets();
    }

    // endregion

    // region Screen

    @Override
    protected void init() {
        super.init();
        initialize();
    }

    @Override
    public void tick() {
        super.tick();
        IScreen.super.tick();
    }

    @Override
    public void onClose() {
        deinitialize();
        super.onClose();
    }

    // endregion

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var maskedMouseX = hasMutexWindow() ? -1 : mouseX;
        var maskedMouseY = hasMutexWindow() ? -1 : mouseY;

        assert minecraft != null;
        guiGraphics.drawCenteredString(minecraft.font, title, this.width / 2, 4, 0xFFFFFFFF);

        for (var c : getChildren()) {
            if (!(c instanceof IWindow)) {
                c.render(guiGraphics, maskedMouseX, maskedMouseY, partialTick);
            }
        }

        guiGraphics.pose().pushPose();

        if (hasWindow()) {
            IScreen.drawGrayishBackground(guiGraphics);
        }

        for (var w : getWindows()) {
            if (w != getMutexWindow()) {
                w.render(guiGraphics, maskedMouseX, maskedMouseY, partialTick);
            }
        }

        if (hasMutexWindow()) {
            assert getMutexWindow() != null;
            getMutexWindow().render(guiGraphics, mouseX, mouseY, partialTick);
        }

        guiGraphics.pose().popPose();
    }

    // region ContainerEventHandler

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return IScreen.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return IScreen.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return IScreen.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return IScreen.super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return IScreen.super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return IScreen.super.charTyped(codePoint, modifiers);
    }

    // endregion
}
