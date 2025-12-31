package games.moegirl.sinocraft.sinocore.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import games.moegirl.sinocraft.sinocore.api.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class RegistryPlatform {
    @ExpectPlatform
    public static <T> Registry<T> createRegistry(RegistryBuilder<T> builder) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T> IRegistry<T> create(String modId, ResourceKey<Registry<T>> key) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ITabRegistry createTab(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static IMenuRegistry createMenu(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static IScreenRegistry createScreen(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ICommandRegistry createCommand(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ICustomStatRegistry createCustomStat(String modId) {
        throw new AssertionError();
    }
}
