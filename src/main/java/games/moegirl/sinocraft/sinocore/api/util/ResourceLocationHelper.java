package games.moegirl.sinocraft.sinocore.api.util;

import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper {
    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }
}
