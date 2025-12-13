package games.moegirl.sinocraft.sinocore.api.gui.layout;

import games.moegirl.sinocraft.sinocore.api.gui.screen.AbstractMenuScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class AbstractMenuLayoutScreen<T extends AbstractContainerMenu> extends AbstractMenuScreen<T> {
    protected final Layout layout;

    public AbstractMenuLayoutScreen(T menu, Inventory playerInventory, Layout layout) {
        super(menu, playerInventory, layout.getTitle());
        this.layout = layout;
    }

    @Override
    protected void createChildren() {

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

    }
}
