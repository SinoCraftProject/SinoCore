package games.moegirl.sinocraft.sinocore.api.gui.widgets;

import games.moegirl.sinocraft.sinocore.api.gui.AbstractWidgetScreen;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.GuiSprite;
import games.moegirl.sinocraft.sinocore.api.gui.layout.component.ImageButtonEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import org.jetbrains.annotations.NotNull;

public class ImageButtonWidget extends Button {

    private final ImageButtonEntry entry;

    protected boolean pressing = false;

    public ImageButtonWidget(AbstractWidgetScreen<?> screen, ImageButtonEntry entry, @NotNull OnPress onPress) {
        super(entry.getX() + screen.getLeftPos(), entry.getY() + screen.getTopPos(),
                entry.getWidth(), entry.getHeight(), entry.getText(), onPress, DEFAULT_NARRATION);
        this.entry = entry;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GuiSprite sprite;
        if (!isActive()) {
            sprite = entry.getTextureDisabledOrDefault();
        } else if (pressing) {
            sprite = entry.getTexturePressedOrDefault();
        } else if (isHoveredOrFocused()) {
            sprite = entry.getTextureHoverOrDefault();
        } else {
            sprite = entry.getTexture();
        }
        guiGraphics.blitSprite(sprite.getPath(), this.getX(), this.getY(), this.width, this.height);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        pressing = true;
        super.onClick(mouseX, mouseY);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        pressing = false;
        super.onRelease(mouseX, mouseY);
    }
}
