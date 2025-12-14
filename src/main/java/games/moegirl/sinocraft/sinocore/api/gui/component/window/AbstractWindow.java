package games.moegirl.sinocraft.sinocore.api.gui.component.window;

import games.moegirl.sinocraft.sinocore.api.gui.component.AbstractComposedComponent;
import net.minecraft.network.chat.Component;

public abstract class AbstractWindow extends AbstractComposedComponent implements IWindow {
    public AbstractWindow(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public IWindowHolder getHolder() {
        return (IWindowHolder) getParent();
    }

    @Override
    public void onOpen() {
        initialize();
    }

    @Override
    public void onClose() {
        deinitialize();
    }
}
