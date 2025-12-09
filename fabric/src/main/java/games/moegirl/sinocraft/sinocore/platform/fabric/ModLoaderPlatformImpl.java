package games.moegirl.sinocraft.sinocore.platform.fabric;

import games.moegirl.sinocraft.sinocore.fabric.util.FabricModList;
import games.moegirl.sinocraft.sinocore.utility.modloader.IModContainer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Optional;

public class ModLoaderPlatformImpl {

    public static Optional<IModContainer> findModById(String modId) {
        return FabricLoader.getInstance().getModContainer(modId).map(FabricModList::new);
    }

    public static boolean isModExists(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
