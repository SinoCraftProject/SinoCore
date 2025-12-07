package games.moegirl.sinocraft.sinocore.neoforge.registry;

import com.mojang.datafixers.util.Pair;
import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.IScreenRegistry;
import games.moegirl.sinocraft.sinocore.neoforge.util.ModBusHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class NeoForgeScreenRegistry implements IScreenRegistry {

    private final String modId;
    private final IEventBus bus;
    private final List<Pair<IRegRef<MenuType<?>>, IScreenFactory<?>>> screens = new ArrayList<>();

    public NeoForgeScreenRegistry(String modId) {
        this.modId = modId;
        this.bus = ModBusHelper.getModBus(modId);
    }

    @Override
    public String modId() {
        return modId;
    }

    @Override
    public void register() {
        bus.addListener((Consumer<RegisterMenuScreensEvent>) event -> {
            for (Pair<IRegRef<MenuType<?>>, IScreenFactory<?>> screen : screens) {
                event.register(screen.getFirst().get(), new ScreenFactoryWrapper(screen.getSecond()));
            }
        });
    }

    @Override
    public <T extends AbstractContainerMenu> void register(IRegRef<MenuType<?>> menuType, IScreenFactory<T> screenFactory) {
        screens.add(Pair.of(menuType, screenFactory));
    }

    static class ScreenFactoryWrapper implements MenuScreens.ScreenConstructor {

        private final IScreenFactory factory;

        public ScreenFactoryWrapper(IScreenFactory<?> factory) {
            this.factory = factory;
        }

        @Override
        public @NotNull Screen create(@NotNull AbstractContainerMenu menu,
                                      @NotNull Inventory inventory, @NotNull Component title) {
            return factory.create(menu, inventory, title);
        }
    }
}
