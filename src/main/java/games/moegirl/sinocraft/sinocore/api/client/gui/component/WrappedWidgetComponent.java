package games.moegirl.sinocraft.sinocore.api.client.gui.component;

import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.function.Consumer;

public class WrappedWidgetComponent extends AbstractComponent {
    protected AbstractWidget innerWidget;

    public WrappedWidgetComponent(AbstractWidget widget) {
        super(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), widget.getMessage());
        this.innerWidget = widget;
    }

    // region LayoutElement

    @Override
    public int getX() {
        return innerWidget.getX();
    }

    @Override
    public void setX(int x) {
        innerWidget.setX(x);
    }

    @Override
    public int getY() {
        return innerWidget.getY();
    }

    @Override
    public void setY(int y) {
        innerWidget.setY(y);
    }

    @Override
    public int getWidth() {
        return innerWidget.getWidth();
    }

    @Override
    public int getHeight() {
        return innerWidget.getHeight();
    }

    @Override
    public ScreenRectangle getRectangle() {
        return innerWidget.getRectangle();
    }

    @Override
    public void setRectangle(int width, int height, int x, int y) {
        innerWidget.setRectangle(width, height, x, y);
    }

    @Override
    public void setPosition(int x, int y) {
        innerWidget.setPosition(x, y);
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        innerWidget.visitWidgets(consumer);
    }

    // endregion

    // region GuiEventListener

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        innerWidget.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return innerWidget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return innerWidget.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return innerWidget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return innerWidget.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return innerWidget.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return innerWidget.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return innerWidget.charTyped(codePoint, modifiers);
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent event) {
        return innerWidget.nextFocusPath(event);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return innerWidget.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void setFocused(boolean focused) {
        innerWidget.setFocused(focused);
    }

    @Override
    public boolean isFocused() {
        return innerWidget.isFocused();
    }

    @Override
    public @Nullable ComponentPath getCurrentFocusPath() {
        return innerWidget.getCurrentFocusPath();
    }

    // endregion

    // region TabOrderedElement

    @Override
    public int getTabOrderGroup() {
        return innerWidget.getTabOrderGroup();
    }

    // endregion

    // region NarratableEntry

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return innerWidget.narrationPriority();
    }

    @Override
    public boolean isActive() {
        return innerWidget.isActive();
    }

    // endregion

    @Override
    public void setWidth(int width) {
        innerWidget.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        innerWidget.setHeight(height);
    }

    @Override
    public void setSize(int width, int height) {
        innerWidget.setSize(width, height);
    }

    @Override
    public int getRight() {
        return innerWidget.getRight();
    }

    @Override
    public int getBottom() {
        return innerWidget.getBottom();
    }

    @Override
    public @Nullable Tooltip getTooltip() {
        return innerWidget.getTooltip();
    }

    @Override
    public void setTooltip(@Nullable Tooltip tooltip) {
        innerWidget.setTooltip(tooltip);
    }

    @Override
    public void setTooltipDelay(Duration tooltipDelay) {
        innerWidget.setTooltipDelay(tooltipDelay);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        innerWidget.onClick(mouseX, mouseY);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        innerWidget.onRelease(mouseX, mouseY);
    }

    @Override
    public void playDownSound(SoundManager handler) {
        innerWidget.playDownSound(handler);
    }

    @Override
    public void setAlpha(float alpha) {
        innerWidget.setAlpha(alpha);
    }

    @Override
    public Component getMessage() {
        return innerWidget.getMessage();
    }

    @Override
    public void setMessage(Component message) {
        innerWidget.setMessage(message);
    }

    @Override
    public boolean isHovered() {
        return innerWidget.isHovered();
    }

    public boolean isHoveredOrFocused() {
        return innerWidget.isHoveredOrFocused();
    }

    @Override
    public void setTabOrderGroup(int tabOrderGroup) {
        innerWidget.setTabOrderGroup(tabOrderGroup);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        innerWidget.updateNarration(narrationElementOutput);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        innerWidget.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
