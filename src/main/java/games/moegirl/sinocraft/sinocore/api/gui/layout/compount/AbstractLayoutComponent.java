package games.moegirl.sinocraft.sinocore.api.gui.layout.compount;

import games.moegirl.sinocraft.sinocore.api.gui.component.AbstractComponent;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.AbstractComponentEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class AbstractLayoutComponent extends AbstractComponent {


    public AbstractLayoutComponent(AS, AbstractComponentEntry entry, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
