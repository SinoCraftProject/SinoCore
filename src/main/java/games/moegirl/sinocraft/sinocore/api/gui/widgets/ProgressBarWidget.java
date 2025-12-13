package games.moegirl.sinocraft.sinocore.api.gui.widgets;

import games.moegirl.sinocraft.sinocore.api.gui.AbstractWidgetScreen;
import games.moegirl.sinocraft.sinocore.api.gui.layout.component.ProgressBarEntry;
import games.moegirl.sinocraft.sinocore.api.gui.layout.component.SpriteEntry;
import games.moegirl.sinocraft.sinocore.utility.GLSwitcher;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleSupplier;

public class ProgressBarWidget extends AbstractWidget {

    private final ProgressBarEntry entry;
    /**
     * 0 ~ 100 for percentage of progress
     */
    private final DoubleSupplier progressSupplier;

    public ProgressBarWidget(AbstractWidgetScreen<?> screen, ProgressBarEntry entry, DoubleSupplier progress) {
        super(entry.getX() + screen.getLeftPos(), entry.getY() + screen.getTopPos(),
                entry.getWidth(), entry.getHeight(), Component.empty());
        this.progressSupplier = progress;
        this.entry = entry;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GLSwitcher b = GLSwitcher.blend();
        if (widgets.containsWidget(entry.getTexture())) {
            SpriteEntry texture = (SpriteEntry) widgets.getWidget(entry.getTexture());
            guiGraphics.blit(widgets.getTexture(), getX(), getY(), getWidth(), getHeight(),
                    texture.getTextureX(), texture.getTextureY(), texture.getTextureWidth(), texture.getTextureHeight(),
                    widgets.getWidth(), widgets.getHeight());
        }
        double progress = getProgress();
        if (progress > 0) {
            SpriteEntry p = (SpriteEntry) widgets.getWidget(entry.getTextureFilled());
            int x = getX(), y = getY();
            int w = p.getWidth(), h = p.getHeight();
            double tu = p.getTextureX(), tv = p.getTextureY();
            int tw = p.getTextureWidth(), th = p.getTextureHeight();
            if (entry.isVertical()) {
                if (entry.isOpposite()) {
                    tv += (1 - progress) * p.getTextureHeight();
                    y += (int) ((1 - progress) * p.getY());
                }
                th = (int) (th * progress);
                h = (int) (h * progress);
            } else {
                if (entry.isOpposite()) {
                    tu += (1 - progress) * p.getTextureWidth();
                    x += (int) ((1 - progress) * p.getWidth());
                }
                tw = (int) (tw * progress);
                w = (int) (w * progress);
            }
            guiGraphics.blit(widgets.getTexture(), x, y, w, h, (float) tu, (float) tv, tw, th, widgets.getWidth(), widgets.getHeight());
        }
        b.disable();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, createNarrationMessage());
    }

    public double getProgress() {
        return progressSupplier.getAsDouble();
    }
}
