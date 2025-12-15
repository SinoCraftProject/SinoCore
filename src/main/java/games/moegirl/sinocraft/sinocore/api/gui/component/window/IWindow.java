package games.moegirl.sinocraft.sinocore.api.gui.component.window;

import games.moegirl.sinocraft.sinocore.api.gui.component.IComposedComponent;

public interface IWindow extends IComposedComponent {
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
