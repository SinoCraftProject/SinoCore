package games.moegirl.sinocraft.sinocore.platform.neoforge;

import games.moegirl.sinocraft.sinocore.neoforge.util.NeoForgeModContainer;
import games.moegirl.sinocraft.sinocore.utility.modloader.IModContainer;

import java.util.Optional;

public class ModLoaderPlatformImpl {
    public static Optional<IModContainer> findModById(String modId) {
        return net.neoforged.fml.ModList.get().getModContainerById(modId).map(NeoForgeModContainer::new);
    }

    public static boolean isModExists(String modId) {
        return net.neoforged.fml.ModList.get().isLoaded(modId);
    }
}
