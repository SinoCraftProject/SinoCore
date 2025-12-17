package games.moegirl.sinocraft.sinocore.platform.fabric;

import games.moegirl.sinocraft.sinocore.api.registry.TabDisplayItemsGenerator;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class TabPlatformImpl {
    public static void onAddTabDisplayItemsGenerator(ResourceKey<CreativeModeTab> key, TabDisplayItemsGenerator generator) {
        ItemGroupEvents.modifyEntriesEvent(key).register(entries -> generator.accept(entries.getContext(), entries));
    }
}
