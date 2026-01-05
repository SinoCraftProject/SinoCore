package games.moegirl.sinocraft.sinocore.fabric.client;

import games.moegirl.sinocraft.sinocore.fabric.client.resource.RenderTypeLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class SinoCoreFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(RenderTypeLoader.INSTANCE);
    }
}
