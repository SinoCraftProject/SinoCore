package games.moegirl.sinocraft.sinocore.api.client.gui.layout;

import games.moegirl.sinocraft.sinocore.api.client.gui.component.*;
import games.moegirl.sinocraft.sinocore.api.gui.Bounds;
import games.moegirl.sinocraft.sinocore.api.gui.layout.Layout;
import games.moegirl.sinocraft.sinocore.api.gui.layout.LayoutManager;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.*;
import games.moegirl.sinocraft.sinocore.api.client.gui.screen.AbstractMenuScreen;
import games.moegirl.sinocraft.sinocore.api.client.gui.screen.AbstractScreen;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class LayoutClientFactories {
    public static AbstractScreen createLayoutScreen(ResourceLocation layoutId) {
        var layout = LayoutManager.getLayout(layoutId);
        var screen = new AbstractScreen(layout.getTitle());
        var layoutWindow = new LayoutWindow(screen, layout);
        screen.addWindow(layoutWindow, true);
        return screen;
    }

    public static <T extends AbstractContainerMenu> AbstractMenuScreen<T> createLayoutMenuScreen(T menu, Inventory playerInventory, ResourceLocation layoutId) {
        var layout = LayoutManager.getLayout(layoutId);
        var screen = new AbstractMenuScreen<>(menu, playerInventory, layout.getTitle());
        var layoutWindow = new LayoutWindow(screen, layout);
        screen.addWindow(layoutWindow, true);
        return screen;
    }

    private static final Map<Class<?>, BiFunction<? extends AbstractComponentEntry, ? extends IComposedComponent, ? extends IComponent>> FACTORIES = new HashMap<>();

    static {
        addComponent(ButtonEntry.class, LayoutClientFactories::createButton);
        addComponent(EditBoxEntry.class, LayoutClientFactories::createEditBox);
        addComponent(ImageButtonEntry.class, LayoutClientFactories::createImageButton);
        addComponent(ProgressBarEntry.class, LayoutClientFactories::createProgressBar);
        addComponent(RectangleEntry.class, LayoutClientFactories::createRectangle);
        addComponent(ImageEntry.class, LayoutClientFactories::createImage);
        addComponent(TextEntry.class, LayoutClientFactories::createText);
    }

    private static <T extends AbstractComponentEntry,
            U extends IComposedComponent,
            V extends IComponent> void addComponent(Class<T> clazz, BiFunction<T, U, V> factory) {
        FACTORIES.put(clazz, factory);
    }

    @SuppressWarnings("unchecked")
    private static <T extends AbstractComponentEntry,
            U extends IComposedComponent,
            V extends IComponent> BiFunction<T, U, V> getFactory(T entry) {
        return (BiFunction<T, U, V>) FACTORIES.get(entry.getClass());
    }

    static <T extends AbstractComponentEntry,
            U extends IComposedComponent,
            V extends IComponent> @Nullable V createComponent(T entry, U parent) {
        if (entry instanceof IServerLayoutComponent) {
            return null;
        }

        BiFunction<T, U, V> factory = getFactory(entry);
        if (factory == null) {
            throw new IllegalArgumentException("No factory for a client component entry: " + entry.getClass().getName());
        }

        return factory.apply(entry, parent);
    }

    private static Bounds getComponentBounds(AbstractComponentEntry entry, IComposedComponent parent) {
        return new Bounds(parent.getX() + entry.getX(), parent.getY() + entry.getY(), entry.getWidth(), entry.getHeight());
    }

    private static ButtonComponent createButton(ButtonEntry entry, IComposedComponent parent) {
        return new ButtonComponent(getComponentBounds(entry, parent), entry.getText(), button -> {
        });
    }

    private static ButtonComponent createImageButton(ImageButtonEntry entry, IComposedComponent parent) {
        return new ImageButtonComponent(getComponentBounds(entry, parent),
                entry.getTexture(), entry.getTextureHover(), entry.getTexturePressed(), entry.getTextureDisabled(),
                button -> {
                });
    }

    private static EditBoxComponent createEditBox(EditBoxEntry entry, IComposedComponent parent) {
        return new EditBoxComponent(parent.getMinecraft().font, getComponentBounds(entry, parent),
                entry.getTitle(), entry.getHint(), entry.getHint(), entry.getSuggestion(), entry.getPlaceholder(),
                entry.getMaxLength(), entry.getColor(), entry.getUneditableColor(), entry.isBordered());
    }

    private static ProgressBarComponent createProgressBar(ProgressBarEntry entry, IComposedComponent parent) {
        return new ProgressBarComponent(getComponentBounds(entry, parent), entry.getDirection(),
                entry.getTexture(), entry.getTextureFilled(), () -> 0.7);
    }

    private static RectangleComponent createRectangle(RectangleEntry entry, IComposedComponent parent) {
        return new RectangleComponent(getComponentBounds(entry, parent), entry.getColor(),
                entry.getBorder(), entry.getBorderColor());
    }

    private static ImageComponent createImage(ImageEntry entry, IComposedComponent parent) {
        return new ImageComponent(getComponentBounds(entry, parent), entry.getImage());
    }

    private static IComponent createText(TextEntry entry, IComposedComponent parent) {
        var bounds = getComponentBounds(entry, parent);
        var widget = new StringWidget(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), entry.getText(), parent.getMinecraft().font);
        return new WrappedWidgetComponent(widget);
    }
}
