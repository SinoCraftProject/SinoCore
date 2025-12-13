package games.moegirl.sinocraft.sinocore.api.gui.component.window;

import games.moegirl.sinocraft.sinocore.api.gui.component.IComponent;

public interface IWindow extends IComponent {
    default void onOpen() {
    }
    default void onClose() {
    }

    default void onShown() {
    }
    default void onHidden() {
    }

    IWindowHolder getHolder();
}
