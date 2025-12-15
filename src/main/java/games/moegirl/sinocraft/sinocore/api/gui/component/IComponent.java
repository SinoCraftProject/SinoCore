package games.moegirl.sinocraft.sinocore.api.gui.component;

import games.moegirl.sinocraft.sinocore.api.gui.Position;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface IComponent extends Renderable, GuiEventListener, LayoutElement, NarratableEntry {
    /**
     * Update in every tick.
     */
    default void tick() {
    }

    /**
     * Update, but invoke manually.
     */
    default void update() {
    }

    default void initialize() {
    }

    default void deinitialize() {
    }


    @Nullable IComposedComponent getParent();
    void setParent(@Nullable IComposedComponent parent);

    boolean isHovered();

    boolean isVisible();
    void setVisible(boolean visible);

    // region LayoutElement

    @Override
    default void visitWidgets(Consumer<AbstractWidget> consumer) {
    }

    @Override
    default ScreenRectangle getRectangle() {
        return LayoutElement.super.getRectangle();
    }

    default void setPosition(Position pos) {
        setPosition(pos.getX(), pos.getY());
    }

    // endregion

    // region NarratableEntry

    @Override
    default NarrationPriority narrationPriority() {
        if (this.isFocused()) {
            return NarrationPriority.FOCUSED;
        } else {
            return isHovered() ? NarrationPriority.HOVERED : NarrationPriority.NONE;
        }
    }

    @Override
    default void updateNarration(NarrationElementOutput narrationElementOutput) {
    }

    // endregion
}
