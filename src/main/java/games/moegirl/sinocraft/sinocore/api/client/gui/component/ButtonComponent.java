package games.moegirl.sinocraft.sinocore.api.client.gui.component;

import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ButtonComponent extends Button implements IComponent {
    @Nullable
    private IComposedComponent parent;

    public ButtonComponent(Bounds bounds, Component message, OnPress onPress) {
        this(bounds, message, onPress, DEFAULT_NARRATION);
    }

    public ButtonComponent(Bounds bounds, Component message, OnPress onPress, CreateNarration createNarration) {
        super(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), message, onPress, createNarration);
    }

    @Override
    public @Nullable IComposedComponent getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable IComposedComponent parent) {
        this.parent = parent;
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
