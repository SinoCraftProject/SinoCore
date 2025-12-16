package games.moegirl.sinocraft.sinocore.api.gui.component;

import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.GuiTexture;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ImageButtonComponent extends ButtonComponent {

    protected final GuiTexture texture;
    protected final GuiTexture hoverTexture;
    protected final GuiTexture pressTexture;
    protected final GuiTexture disabledTexture;

    protected boolean pressing = false;

    public ImageButtonComponent(Bounds bounds, GuiTexture texture, GuiTexture hoverTexture,
                                GuiTexture pressTexture, GuiTexture disabledTexture, OnPress onPress) {
        this(bounds, texture, hoverTexture, pressTexture, disabledTexture, onPress, DEFAULT_NARRATION);
    }

    public ImageButtonComponent(Bounds bounds, GuiTexture texture, GuiTexture hoverTexture,
                                GuiTexture pressTexture, GuiTexture disabledTexture,
                                OnPress onPress, CreateNarration createNarration) {
        super(bounds, Component.empty(), onPress, createNarration);
        this.texture = texture;
        this.hoverTexture = hoverTexture;
        this.pressTexture = pressTexture;
        this.disabledTexture = disabledTexture;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GuiTexture sprite;
        if (!isActive()) {
            sprite = disabledTexture;
        } else if (pressing) {
            sprite = pressTexture;
        } else if (isHoveredOrFocused()) {
            sprite = hoverTexture;
        } else {
            sprite = texture;
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
