package games.moegirl.sinocraft.sinocore.platform.neoforge;

import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.registry.TabDisplayItemsGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.Objects;

@EventBusSubscriber(modid = SinoCore.MODID)
public class TabPlatformImpl {
    public static void onAddTabDisplayItemsGenerator(ResourceKey<CreativeModeTab> key, TabDisplayItemsGenerator generator) {
    }

    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        for (var entry : TabDisplayItemsGenerator.getUnregisteredGenerators().entrySet()) {
            var key = entry.getKey();
            var generator = entry.getValue();
            if (Objects.equals(key, event.getTabKey())) {
                generator.accept(event.getParameters(), event);
            }
        }
    }
}
