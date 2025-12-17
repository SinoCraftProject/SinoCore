package games.moegirl.sinocraft.sinocore.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import games.moegirl.sinocraft.sinocore.api.registry.TabDisplayItemsGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class TabPlatform {
    @ExpectPlatform
    public static void onAddTabDisplayItemsGenerator(ResourceKey<CreativeModeTab> key, TabDisplayItemsGenerator generator) {
        throw new AssertionError();
    }
}
