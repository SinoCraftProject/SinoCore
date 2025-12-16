package games.moegirl.sinocraft.sinocore.api.client.gui.layout;

import games.moegirl.sinocraft.sinocore.api.client.gui.component.*;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.layout.Layout;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.*;
import games.moegirl.sinocraft.sinocore.api.client.gui.screen.AbstractMenuScreen;
import games.moegirl.sinocraft.sinocore.api.client.gui.screen.AbstractScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class LayoutFactories {

    public static AbstractScreen createLayoutScreen(Layout layout) {
        var screen = new AbstractScreen(layout.getTitle());
        var layoutWindow = new LayoutWindow(screen, layout);
        screen.addWindow(layoutWindow, true);
        return screen;
    }

    public static <T extends AbstractContainerMenu> AbstractMenuScreen<T> createLayoutMenuScreen(T menu, Inventory playerInventory, Layout layout) {
        var screen = new AbstractMenuScreen<>(menu, playerInventory, layout.getTitle());
        var layoutWindow = new LayoutWindow(screen, layout);
        screen.addWindow(layoutWindow, true);
        return screen;
    }

    private static Bounds getComponentBounds(AbstractComponentEntry entry, IComposedComponent parent) {
        return new Bounds(parent.getX() + entry.getX(), parent.getY() + entry.getY(), entry.getWidth(), entry.getHeight());
    }

    public static ButtonComponent createButton(ButtonEntry entry, IComposedComponent parent) {
        return new ButtonComponent(getComponentBounds(entry, parent), entry.getText(), button -> {
        });
    }

    public static ButtonComponent createImageButton(ImageButtonEntry entry, IComposedComponent parent) {
        return new ImageButtonComponent(getComponentBounds(entry, parent),
                entry.getTexture(), entry.getTextureHover(), entry.getTexturePressed(), entry.getTextureDisabled(),
                button -> {
                });
    }

    public static EditBoxComponent createEditBox(EditBoxEntry entry, IComposedComponent parent) {
        return new EditBoxComponent(parent.getMinecraft().font, getComponentBounds(entry, parent),
                entry.getTitle(), entry.getHint(), entry.getHint(), entry.getSuggestion(), entry.getPlaceholder(),
                entry.getMaxLength(), entry.getColor(), entry.getUneditableColor(), entry.isBordered());
    }

    public static ProgressBarComponent createProgressBar(ProgressBarEntry entry, IComposedComponent parent) {
        return new ProgressBarComponent(getComponentBounds(entry, parent), entry.getDirection(),
                entry.getTexture(), entry.getTextureFilled(), () -> 0.7);
    }

    public static RectangleComponent createRectangle(RectangleEntry entry, IComposedComponent parent) {
        return new RectangleComponent(getComponentBounds(entry, parent), entry.getColor(),
                entry.getBorder(), entry.getBorderColor());
    }

    public static ImageComponent createImage(ImageEntry entry, IComposedComponent parent) {
        return new ImageComponent(getComponentBounds(entry, parent), entry.getImage());
    }
}
