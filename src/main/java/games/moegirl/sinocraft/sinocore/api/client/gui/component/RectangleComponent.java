package games.moegirl.sinocraft.sinocore.api.client.gui.component;

import games.moegirl.sinocraft.sinocore.api.gui.Border;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import net.minecraft.client.gui.GuiGraphics;

public class RectangleComponent extends AbstractComponent {
    protected int color;
    protected Border borderWidth;
    protected int borderColor;

    public RectangleComponent(Bounds bounds, int color, Border border, int borderColor) {
        super(bounds);
        this.color = color;
        this.borderWidth = border;
        this.borderColor = borderColor;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var maxX = getX() + getWidth();
        var maxY = getY() + getHeight();

        guiGraphics.fill(getX(), getY(), maxX, maxY, color);
        if (borderWidth.getTop() > 0) {
            guiGraphics.fill(getX(), getY(), maxX, getY() + borderWidth.getTop(), borderColor);
        }

        if (borderWidth.getRight() > 0) {
            guiGraphics.fill(maxX - borderWidth.getRight(), getY(), maxX, maxY, borderColor);
        }

        if (borderWidth.getBottom() > 0) {
            guiGraphics.fill(getX(), maxY - borderWidth.getBottom(), maxX, maxY, borderColor);
        }

        if (borderWidth.getLeft() > 0) {
            guiGraphics.fill(getX(), getY(), getX() + borderWidth.getLeft(), maxY, borderColor);
        }
    }
}
