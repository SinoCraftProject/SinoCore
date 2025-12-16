package games.moegirl.sinocraft.sinocore.api.client.gui.component;

import net.minecraft.client.gui.components.events.ContainerEventHandler;

import java.util.List;

public interface IComposedComponent extends IComponent, ContainerEventHandler {
    void addChild(IComponent child);

    void removeChild(IComponent child);

    /**
     * Get children components.
     * @return An immutable list of children.
     */
    List<IComponent> getChildren();

    default void clearChildren() {
        for (var c : getChildren()) {
            c.setParent(null);
            removeChild(c);
        }
    }

    default void createChildren() {
    }

    @Override
    default void initialize() {
        IComponent.super.initialize();

        for (var child : getChildren()) {
            child.initialize();
        }
    }

    @Override
    default void deinitialize() {
        for (var child : getChildren()) {
            child.deinitialize();
        }

        IComponent.super.deinitialize();
    }

    @Override
    default void tick() {
        IComponent.super.tick();

        for (var child : getChildren()) {
            child.tick();
        }
    }

    @Override
    default void update() {
        IComponent.super.update();

        for (var child : getChildren()) {
            child.update();
        }
    }

    // region ContainerEventHandler

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return ContainerEventHandler.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        return ContainerEventHandler.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return ContainerEventHandler.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return ContainerEventHandler.super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        ContainerEventHandler.super.mouseMoved(mouseX, mouseY);
    }

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return ContainerEventHandler.super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return ContainerEventHandler.super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean charTyped(char codePoint, int modifiers) {
        return ContainerEventHandler.super.charTyped(codePoint, modifiers);
    }

    // endregion
}
