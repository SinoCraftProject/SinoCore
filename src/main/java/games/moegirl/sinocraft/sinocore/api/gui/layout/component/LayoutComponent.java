package games.moegirl.sinocraft.sinocore.api.gui.layout.component;

import games.moegirl.sinocraft.sinocore.api.gui.component.AbstractComposedComponent;
import games.moegirl.sinocraft.sinocore.api.gui.layout.ILayoutComponent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class LayoutComponent extends AbstractComposedComponent implements ILayoutComponent {
    protected int startX;
    protected int startY;

    public LayoutComponent(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void initialize() {
        ILayoutComponent.super.initialize();
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    @Override
    public void createChildren() {

    }
}
