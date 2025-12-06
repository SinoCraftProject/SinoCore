package games.moegirl.sinocraft.sinocore.api.util;

import games.moegirl.sinocraft.sinocore.SinoCorePlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;

public class ResourceManagerHelper {
    public static ResourceManager getResourceManager() {
        if (SinoCorePlatform.isClientDist()) {
            return Minecraft.getInstance().getResourceManager();
        } else {
            return SinoCorePlatform.getServer().getResourceManager();
        }
    }
}
