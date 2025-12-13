package games.moegirl.sinocraft.sinocore.api.gui;

import games.moegirl.sinocraft.sinocore.api.gui.layout.component.*;
import games.moegirl.sinocraft.sinocore.api.gui.layout.Layout;
import games.moegirl.sinocraft.sinocore.api.gui.widgets.component.EditBoxWidget;
import games.moegirl.sinocraft.sinocore.api.gui.widgets.component.ImageButtonWidget;
import games.moegirl.sinocraft.sinocore.api.gui.widgets.component.ProgressBarWidget;
import games.moegirl.sinocraft.sinocore.utility.GLSwitcher;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

public class AbstractWidgetScreen<T extends AbstractWidgetMenu> extends AbstractContainerScreen<T> {

    protected final Layout widgets;

    public AbstractWidgetScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.widgets = menu.widgets;

        if (widgets.containsWidget("background")) {
            SpriteEntry entry = (SpriteEntry) widgets.getWidget("background");
            imageWidth = entry.getWidth();
            imageHeight = entry.getHeight();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // qyl27: Do nothing. To prevent render "Inventory" in our GUI.
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        if (widgets.containsWidget("background")) {
            blitTexture(guiGraphics, "background", 0, 0);
        }
    }

    protected ImageButtonWidget addButton(String name, Button.OnPress onPress) {
        ImageButtonEntry entry = (ImageButtonEntry) widgets.getWidget(name);
        ImageButtonWidget button = new ImageButtonWidget(this, entry, onPress);
        addRenderableWidget(button);
        return button;
    }

    protected EditBoxWidget addEditBox(String name, Font font) {
        EditBoxEntry entry = (EditBoxEntry) widgets.getWidget(name);
        EditBoxWidget editBox = new EditBoxWidget(font, entry, leftPos, topPos);
        addRenderableWidget(editBox);
        editBox.initializeFocus(this);
        return editBox;
    }

    protected EditBoxWidget addEditBox(String name, Font font, Consumer<String> responder) {
        EditBoxWidget editBox = addEditBox(name, font);
        editBox.setResponder(responder);
        return editBox;
    }

    protected EditBoxWidget addEditBox(String name) {
        return addEditBox(name, font);
    }

    protected EditBoxWidget addEditBox(String name, Consumer<String> responder) {
        return addEditBox(name, font, responder);
    }

    protected ProgressBarWidget addProgress(GuiGraphics guiGraphics, String name, DoubleSupplier progress) {
        ProgressBarEntry entry = (ProgressBarEntry) widgets.getWidget(name);
        ProgressBarWidget widget = new ProgressBarWidget(leftPos, topPos, widgets, entry, progress);
        addRenderableWidget(widget);
        return widget;
    }

    protected void drawText(GuiGraphics guiGraphics, String name, Font font) {
        TextEntry entry = (TextEntry) widgets.getWidget(name);
        if (entry.getText().isEmpty()) {
            if (!entry.getRawText().isEmpty()) {
                drawText(guiGraphics, font, entry, Component.literal(entry.getRawText()));
            }
        } else {
            drawText(guiGraphics, font, entry, Component.translatable(entry.getText()));
        }
    }

    protected void drawText(GuiGraphics guiGraphics, String name) {
        drawText(guiGraphics, name, font);
    }

    private void drawText(GuiGraphics guiGraphics, Font font, TextEntry entry, Component text) {
        int tx = entry.getX();
        int ty = entry.getY();
        if (entry.isCentered()) {
            tx += font.width(text) / 2;
        }
        guiGraphics.drawString(font, text, tx, ty, entry.getColor(), entry.isShadowed());
    }

    public void blitTexture(GuiGraphics guiGraphics, String name) {
        SpriteEntry entry = (SpriteEntry) widgets.getWidget(name);
        blitTexture(guiGraphics, name, entry.getX(), entry.getY(), entry.getWidth(), entry.getHeight());
    }

    public void blitTexture(GuiGraphics guiGraphics, String name, int x, int y) {
        SpriteEntry entry = (SpriteEntry) widgets.getWidget(name);
        blitTexture(guiGraphics, name, x, y, entry.getWidth(), entry.getHeight());
    }

    public void blitTexture(GuiGraphics guiGraphics, String name, int x, int y, int width, int height) {
        SpriteEntry entry = (SpriteEntry) widgets.getWidget(name);
        guiGraphics.blit(widgets.getTexture(), leftPos + x, topPos + y, width, height, entry.getTextureX(), entry.getTextureY(),
                entry.getTextureWidth(), entry.getTextureHeight(), widgets.getWidth(), widgets.getHeight());
    }

    public void blitTexture(GuiGraphics guiGraphics, String name, int x, int y, int width, int height, GLSwitcher... configurations) {
        blitTexture(guiGraphics, name, x, y, width, height);
        for (GLSwitcher switcher : configurations) {
            switcher.resume();
        }
    }

    public int getLeftPos() {
        return leftPos;
    }

    public int getTopPos() {
        return topPos;
    }

    public Font getFont() {
        return font;
    }

    public Layout getWidgets() {
        return widgets;
    }
}
