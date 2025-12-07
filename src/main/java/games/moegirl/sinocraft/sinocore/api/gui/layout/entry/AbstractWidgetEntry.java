package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import games.moegirl.sinocraft.sinocore.api.gui.widgets.WidgetLoader;
import lombok.Getter;

@Getter
public class AbstractWidgetEntry {
    public static final String UNNAMED = "_UNNAMED_WIDGET_";

    protected String name = UNNAMED;
    protected final String type;

    protected AbstractWidgetEntry() {
        this.type = WidgetLoader.getTypeName(getClass());
    }

    public void setName(String name) {
        if (!UNNAMED.equals(this.name)) {
            throw new IllegalStateException("Cannot set name to a widget: already has name " + name);
        }
        this.name = name;
    }
}
