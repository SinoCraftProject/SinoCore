package games.moegirl.sinocraft.sinocore.fabric.registry;

import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.ITabRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.TabDisplayItemsGenerator;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FabricTabRegistry extends FabricRegistry<CreativeModeTab> implements ITabRegistry {

    public FabricTabRegistry(String modId) {
        super(modId, Registries.CREATIVE_MODE_TAB);
    }

    @Override
    public <R extends CreativeModeTab> IRegRef<R> register(String name, Supplier<? extends R> supplier) {
        throw new AssertionError();
    }

    @Override
    public IRegRef<CreativeModeTab> registerTab(String name, Consumer<CreativeModeTab.Builder> consumer) {
        var generator = new TabDisplayItemsGenerator();
        var builder = FabricItemGroup.builder();
        consumer.accept(builder);
        var tab = builder.build();
        var ref = new FabricRegRef<>(Registry.registerForHolder(registry, createId(name), tab));
        elements.put(ResourceLocation.fromNamespaceAndPath(modId, name), ref);
        TabDisplayItemsGenerator.setRegisteredGenerators(ref.getKey(), generator);
        return ref;
    }
}
