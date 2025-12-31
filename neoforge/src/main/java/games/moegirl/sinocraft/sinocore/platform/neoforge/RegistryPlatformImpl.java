package games.moegirl.sinocraft.sinocore.platform.neoforge;

import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.registry.*;
import games.moegirl.sinocraft.sinocore.neoforge.registry.*;
import games.moegirl.sinocraft.sinocore.neoforge.util.ModBusHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.function.Consumer;

public class RegistryPlatformImpl {
    public static <T> Registry<T> createRegistry(RegistryBuilder<T> builder) {
        var b = new net.neoforged.neoforge.registries.RegistryBuilder<T>(builder.getKey());
        b.sync(builder.isSync());
        var registry = b.create();

        var bus = ModBusHelper.getModBus(SinoCore.MODID);
        bus.addListener((Consumer<NewRegistryEvent>) event -> event.register(registry));

        return registry;
    }

    public static <T> IRegistry<T> create(String modId, ResourceKey<Registry<T>> key) {
        return new NeoForgeRegistry<>(modId, key);
    }

    public static ITabRegistry createTab(String modId) {
        return new NeoForgeTabRegistry(modId);
    }

    public static IMenuRegistry createMenu(String modId) {
        return new NeoForgeMenuRegistry(modId);
    }

    public static IScreenRegistry createScreen(String modId) {
        return new NeoForgeScreenRegistry(modId);
    }

    public static ICommandRegistry createCommand(String modId) {
        return new NeoForgeCommandRegister(modId);
    }

    public static ICustomStatRegistry createCustomStat(String modId) {
        return new NeoForgeCustomStatRegistry(modId);
    }
}
