package games.moegirl.sinocraft.sinocore.api.gui.layout;

import games.moegirl.sinocraft.sinocore.api.gui.component.IComposedComponent;

public interface ILayoutComponent extends IComposedComponent {
    int getStartX();
    int getStartY();
    void createChildren();
}
