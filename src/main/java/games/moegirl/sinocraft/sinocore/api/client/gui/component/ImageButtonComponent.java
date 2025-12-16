package games.moegirl.sinocraft.sinocore.api.client.gui.component;

import games.moegirl.sinocraft.sinocore.api.client.gui.GuiImageRender;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.GuiImage;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

@Getter
public class ImageButtonComponent extends ButtonComponent {

    protected GuiImage texture;
    protected GuiImage hoverTexture;
    protected GuiImage pressTexture;
    protected GuiImage disabledTexture;

    protected boolean pressing = false;

    public ImageButtonComponent(Bounds bounds, GuiImage texture, GuiImage hoverTexture,
                                GuiImage pressTexture, GuiImage disabledTexture, OnPress onPress) {
        this(bounds, texture, hoverTexture, pressTexture, disabledTexture, onPress, DEFAULT_NARRATION);
    }

    public ImageButtonComponent(Bounds bounds, GuiImage texture, GuiImage hoverTexture,
                                GuiImage pressTexture, GuiImage disabledTexture,
                                OnPress onPress, CreateNarration createNarration) {
        super(bounds, Component.empty(), onPress, createNarration);
        this.texture = texture;
        this.hoverTexture = hoverTexture;
        this.pressTexture = pressTexture;
        this.disabledTexture = disabledTexture;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GuiImage image;
        if (!isActive()) {
            image = disabledTexture;
        } else if (pressing) {
            image = pressTexture;
        } else if (isHoveredOrFocused()) {
            image = hoverTexture;
        } else {
            image = texture;
        }
        GuiImageRender.blitImage(image, guiGraphics, getX(), getY(), getWidth(), getHeight());
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
