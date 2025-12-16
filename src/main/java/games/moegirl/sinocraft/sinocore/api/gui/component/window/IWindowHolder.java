package games.moegirl.sinocraft.sinocore.api.gui.component.window;

import games.moegirl.sinocraft.sinocore.api.gui.component.IComposedComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IWindowHolder extends IComposedComponent {
    List<IWindow> getWindows();
    void addWindow(IWindow window, boolean modal, boolean show);
    void closeWindow(IWindow window);

    void show(IWindow window);
    void hide(IWindow window);

    @Nullable
    IWindow getModalWindow();

    @Nullable
    IWindow getFocusedWindow();
    void setFocusedWindow(@Nullable IWindow window);

    default void addWindow(IWindow window) {
        addWindow(window, false);
    }

    default void addWindow(IWindow window, boolean modal) {
        addWindow(window, modal, true);
    }

    default boolean isFocused(IWindow window) {
        return getFocusedWindow() == window;
    }

    default boolean isModalWindow(IWindow window) {
        return getModalWindow() == window;
    }

    default boolean hasModalWindow() {
        return getModalWindow() != null;
    }

    default boolean hasWindow() {
        return !getWindows().isEmpty();
    }

    default boolean hasWindow(IWindow window) {
        return getWindows().contains(window);
    }

    default void closeAllWindows() {
        for (var window : getWindows()) {
            closeWindow(window);
        }
    }
}
