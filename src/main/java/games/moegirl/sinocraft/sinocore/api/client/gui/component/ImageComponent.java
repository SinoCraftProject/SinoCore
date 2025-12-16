package games.moegirl.sinocraft.sinocore.api.client.gui.component;

import games.moegirl.sinocraft.sinocore.api.client.gui.GuiImageRender;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.GuiImage;
import net.minecraft.client.gui.GuiGraphics;

public class ImageComponent extends AbstractComponent {
    protected GuiImage image;

    public ImageComponent(Bounds bounds, GuiImage image) {
        super(bounds);
        this.image = image;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GuiImageRender.blitImage(image, guiGraphics, getX(), getY(), getWidth(), getHeight());
    }
}
