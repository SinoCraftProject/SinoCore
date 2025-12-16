package games.moegirl.sinocraft.sinocore.api.gui.component;

import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class EditBoxComponent extends EditBox implements IComponent {
    @Nullable
    private IComposedComponent parent;

    public EditBoxComponent(Font font, Bounds bounds, Component title,
                            @Nullable Component tooltip, @Nullable Component hint,
                            @Nullable String suggestion, @Nullable String placeholder, int maxLength,
                            int textColor, int uneditableTextColor, boolean isBordered) {
        super(font, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), title);
        if (tooltip != null) {
            setTooltip(Tooltip.create(tooltip));
        }
        if (hint != null) {
            setHint(hint);
        }
        if (suggestion != null) {
            setSuggestion(suggestion);
        }
        if (placeholder != null) {
            setValue(placeholder);
        }
        setMaxLength(maxLength);
        setTextColor(textColor);
        setTextColorUneditable(uneditableTextColor);
        setBordered(isBordered);
    }

    @Override
    public @Nullable IComposedComponent getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable IComposedComponent parent) {
        this.parent = parent;
    }
}
