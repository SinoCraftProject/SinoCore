package games.moegirl.sinocraft.sinocore.platform.fabric;

import games.moegirl.sinocraft.sinocore.api.registry.*;
import games.moegirl.sinocraft.sinocore.fabric.registry.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class RegistryPlatformImpl {
    public static <T> Registry<T> createRegistry(RegistryBuilder<T> builder) {
        var b = FabricRegistryBuilder.createSimple(builder.getKey());
        if (builder.isSync()) {
            b.attribute(RegistryAttribute.SYNCED);
        }
        return b.buildAndRegister();
    }

    public static <T> IRegistry<T> create(String modId, ResourceKey<Registry<T>> key) {
        return new FabricRegistry<>(modId, key);
    }

    public static ITabRegistry createTab(String modId) {
        return new FabricTabRegistry(modId);
    }

    public static IMenuRegistry createMenu(String modId) {
        return new FabricMenuRegistry(modId);
    }

    public static IScreenRegistry createScreen(String modId) {
        return new FabricScreenRegistry(modId);
    }

    public static ICommandRegistry createCommand(String modId) {
        return new FabricCommandRegister(modId);
    }

    public static ICustomStatRegistry createCustomStat(String modId) {
        return new FabricCustomStatRegistry(modId);
    }
}
