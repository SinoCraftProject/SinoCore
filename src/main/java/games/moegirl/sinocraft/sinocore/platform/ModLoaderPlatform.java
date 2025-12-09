package games.moegirl.sinocraft.sinocore.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import games.moegirl.sinocraft.sinocore.utility.modloader.IModContainer;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

@ApiStatus.Internal
public class ModLoaderPlatform {
    @ExpectPlatform
    public static Optional<IModContainer> findModById(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModExists(String modId) {
        throw new AssertionError();
    }
}
