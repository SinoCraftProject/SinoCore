package games.moegirl.sinocraft.sinocore.api.gui.widgets;

import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.EditBoxEntry;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class EditBoxWidget extends EditBox {

    public EditBoxWidget(Font font, EditBoxEntry entry, int leftPos, int topPos) {
        super(font, leftPos + entry.getX(), topPos + entry.getY(), entry.getWidth(), entry.getHeight(), entry.getTitle());
        entry.getHint().ifPresent(this::setHint);
        entry.getSuggestion().ifPresent(this::setSuggestion);
        entry.getPlaceholder().ifPresent(this::setValue);
        entry.getTooltip().ifPresent(t -> setTooltip(Tooltip.create(t)));
        setMaxLength(entry.getMaxLength());
        setTextColor(entry.getColor());
        setTextColorUneditable(entry.getUneditableColor());
        setAlpha(entry.getAlpha());
        setBordered(entry.isBordered());
    }

    public void initializeFocus(AbstractContainerScreen<?> screen) {
        ComponentPath path = ComponentPath.path(screen, screen.nextFocusPath(new FocusNavigationEvent.InitialFocus()));
        if (path != null) {
            if (screen.getCurrentFocusPath() != null) {
                screen.getCurrentFocusPath().applyFocus(false);
            }
            path.applyFocus(true);
        }
    }
}
