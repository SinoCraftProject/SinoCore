package games.moegirl.sinocraft.sinocore.api.client.gui.component;

import games.moegirl.sinocraft.sinocore.api.client.gui.GuiImageRender;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.Direction2D;
import games.moegirl.sinocraft.sinocore.api.gui.GuiImage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.function.DoubleSupplier;

public class ProgressBarComponent extends AbstractComponent {

    private final Direction2D direction;
    private final GuiImage texture;
    private final GuiImage filledTexture;

    /**
     * 0 ~ 1 for percentage of progress
     */
    private final DoubleSupplier progressSupplier;

    public ProgressBarComponent(Bounds bounds,
                                Direction2D direction, GuiImage texture, GuiImage filledTexture,
                                DoubleSupplier progress) {
        super(bounds);
        this.direction = direction;
        this.texture = texture;
        this.filledTexture = filledTexture;
        this.progressSupplier = progress;
    }

    public double getProgress() {
        return progressSupplier.getAsDouble();
    }

    protected int getProgressOf(int total) {
        return (int) (total * getProgress());
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GuiImageRender.blitImage(texture, guiGraphics, getX(), getY(), getWidth(), getHeight());

        guiGraphics.pose().pushPose();

        var maxX = getX() + getWidth();
        var maxY = getY() + getHeight();
        if (direction.isHorizontal()) {
            if (!direction.isReversed()) {
                var x = getX() + getProgressOf(getWidth());
                guiGraphics.enableScissor(getX(), getY(), x, maxY);
            } else {
                var x = getX() + getWidth() - getProgressOf(getWidth());
                guiGraphics.enableScissor(x, getY(), maxX, maxY);
            }
        } else {
            if (!direction.isReversed()) {
                var y = getY() + getProgressOf(getHeight());
                guiGraphics.enableScissor(getX(), getY(), maxX, y);
            } else {
                var y = getY() + getHeight() - getProgressOf(getHeight());
                guiGraphics.enableScissor(getX(), y, maxX, maxY);
            }
        }

        GuiImageRender.blitImage(filledTexture, guiGraphics, getX(), getY(), getWidth(), getHeight());
        guiGraphics.disableScissor();

        guiGraphics.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, createNarrationMessage());
    }
}
