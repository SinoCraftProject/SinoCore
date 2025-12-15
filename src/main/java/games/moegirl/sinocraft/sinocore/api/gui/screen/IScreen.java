package games.moegirl.sinocraft.sinocore.api.gui.screen;

import games.moegirl.sinocraft.sinocore.api.gui.component.IComposedComponent;
import games.moegirl.sinocraft.sinocore.api.gui.component.window.IWindowHolder;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import javax.naming.OperationNotSupportedException;

public interface IScreen extends IWindowHolder {

    // region IComponent

    @Override
    default @Nullable IComposedComponent getParent() {
        return null;
    }

    @Override
    default void setParent(@Nullable IComposedComponent parent) {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    @Override
    default void setX(int x) {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    @Override
    default void setY(int y) {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    @Override
    default int getX() {
        return 0;
    }

    @Override
    default int getY() {
        return 0;
    }

    // Screen is always been hovered.
    @Override
    default boolean isHovered() {
        return true;
    }

    @Override
    default boolean isVisible() {
        return true;
    }

    @Override
    default void setVisible(boolean visible) {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    // endregion

    @Override
    default void tick() {
        for (var c : getChildren()) {
            c.tick();
        }

        IWindowHolder.super.tick();
    }

    static void drawGrayishBackground(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.fillGradient(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), -1072689136, -804253680);
        guiGraphics.pose().popPose();
    }

    // region ContainerEventHandler

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (hasModalWindow()) {
            assert getModalWindow() != null;
            return getModalWindow().mouseClicked(mouseX, mouseY, button);
        }
        return IWindowHolder.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (hasModalWindow()) {
            assert getModalWindow() != null;
            return getModalWindow().mouseReleased(mouseX, mouseY, button);
        }
        return IWindowHolder.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (hasModalWindow()) {
            assert getModalWindow() != null;
            return getModalWindow().mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return IWindowHolder.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (hasModalWindow()) {
            assert getModalWindow() != null;
            return getModalWindow().mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }
        return IWindowHolder.super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        if (hasModalWindow()) {
            assert getModalWindow() != null;
            getModalWindow().mouseMoved(mouseX, mouseY);
        }
        IWindowHolder.super.mouseMoved(mouseX, mouseY);
    }

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (hasModalWindow()) {
            assert getModalWindow() != null;
            return getModalWindow().keyPressed(keyCode, scanCode, modifiers);
        }
        return IWindowHolder.super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (hasModalWindow()) {
            assert getModalWindow() != null;
            return getModalWindow().keyReleased(keyCode, scanCode, modifiers);
        }
        return IWindowHolder.super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean charTyped(char codePoint, int modifiers) {
        if (hasModalWindow()) {
            assert getModalWindow() != null;
            return getModalWindow().charTyped(codePoint, modifiers);
        }
        return IWindowHolder.super.charTyped(codePoint, modifiers);
    }

    // endregion
}
