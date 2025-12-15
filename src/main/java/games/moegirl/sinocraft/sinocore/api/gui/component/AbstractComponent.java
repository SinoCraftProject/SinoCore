package games.moegirl.sinocraft.sinocore.api.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractComponent extends AbstractWidget implements IComponent {

    @Nullable
    private IComposedComponent parent;

    public AbstractComponent(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public @Nullable IComposedComponent getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable IComposedComponent parent) {
        this.parent = parent;
    }

    protected final Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
