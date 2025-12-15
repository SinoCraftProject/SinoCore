package games.moegirl.sinocraft.sinocore.api.gui.screen;

import games.moegirl.sinocraft.sinocore.api.gui.component.IComponent;
import games.moegirl.sinocraft.sinocore.api.gui.component.window.IWindow;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractMenuScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IScreen {
    public AbstractMenuScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        createChildren();
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

    // region IComposedComponent

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

    // endregion

    // region Window holder.

    /**
     * Map<IWindow window, Boolean shown>
     */
    private final Map<IWindow, Boolean> windows = new LinkedHashMap<>();

    @Nullable
    private IWindow modalWindow = null;

    @Nullable
    private IWindow focusedWindow = null;

    @Override
    public List<IWindow> getWindows() {
        return List.copyOf(windows.keySet());
    }

    @Override
    public void addWindow(IWindow window, boolean modal, boolean show) {
        addChild(window);
        windows.put(window, false);
        window.onOpen();

        if (modal) {
            setModalWindow(window);
        }

        if (show) {
            show(window);
        }
    }

    @Override
    public void closeWindow(IWindow window) {
        if (isModalWindow(window)) {
            setModalWindow(null);
        }

        if (isFocused(window)) {
            setFocusedWindow(null);
        }

        window.onClose();
        windows.remove(window);
        removeChild(window);
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
        if (getModalWindow() == window) {
            throw new UnsupportedOperationException("Modal window cannot be hidden.");
        }

        if (windows.get(window)) {
            windows.put(window, false);
            window.onHidden();
        }
    }

    @Override
    public @Nullable IWindow getModalWindow() {
        return modalWindow;
    }

    @Override
    public void setModalWindow(@Nullable IWindow window) {
        if (window != null && hasWindow(window)) {
            if (hasModalWindow()) {
                throw new IllegalStateException("A modal window is already present.");
            }

            setFocusedWindow(window);
            modalWindow = window;
        } else {
            modalWindow = null;
        }
    }

    @Override
    public @Nullable IWindow getFocusedWindow() {
        return focusedWindow;
    }

    @Override
    public void setFocusedWindow(@Nullable IWindow window) {
        if (hasModalWindow()) {
            setFocused(getModalWindow());
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

    // endregion

    // region Screen

    @Override
    public List<? extends GuiEventListener> children() {
        return List.copyOf(getChildren());
    }

    @Override
    protected void rebuildWidgets() {
        this.deinitialize();
        super.rebuildWidgets();
    }

    @Override
    protected void init() {
        super.init();
        initialize();
    }

    @Override
    protected void containerTick() {
        for (var c : getChildren()) {
            c.tick();
        }

        super.containerTick();
    }

    @Override
    public void onClose() {
        this.deinitialize();
        super.onClose();
    }

    // endregion

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var maskedMouseX = hasModalWindow() ? -1 : mouseX;
        var maskedMouseY = hasModalWindow() ? -1 : mouseY;

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
            if (w != getModalWindow()) {
                w.render(guiGraphics, maskedMouseX, maskedMouseY, partialTick);
            }
        }

        if (hasModalWindow()) {
            assert getModalWindow() != null;
            getModalWindow().render(guiGraphics, mouseX, mouseY, partialTick);
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
