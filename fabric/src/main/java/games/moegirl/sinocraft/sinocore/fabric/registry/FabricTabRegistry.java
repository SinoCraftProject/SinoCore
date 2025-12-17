package games.moegirl.sinocraft.sinocore.fabric.registry;

import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.ITabRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.TabDisplayItemsGenerator;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class FabricTabRegistry implements ITabRegistry {

    private final String modId;
    private final Registry<CreativeModeTab> registry;
    private final Map<ResourceLocation, IRegRef<CreativeModeTab>> elements = new HashMap<>();

    @SuppressWarnings("unchecked")
    public FabricTabRegistry(String modId) {
        this.modId = modId;
        registry = (Registry<CreativeModeTab>) BuiltInRegistries.REGISTRY.get(Registries.CREATIVE_MODE_TAB.location());
    }

    @Override
    public String modId() {
        return modId;
    }

    @Override
    public void register() {
    }

    @Override
    public Registry<CreativeModeTab> getRegistry() {
        return registry;
    }

    @Override
    public Iterable<IRegRef<CreativeModeTab>> getEntries() {
        return elements.values();
    }

    @Override
    public Optional<IRegRef<CreativeModeTab>> get(ResourceLocation id) {
        return Optional.ofNullable(elements.get(id));
    }

    @Override
    public IRegRef<CreativeModeTab> registerTab(String name, Consumer<CreativeModeTab.Builder> consumer) {
        var generator = new TabDisplayItemsGenerator();
        var builder = FabricItemGroup.builder();
        consumer.accept(builder);
        var tab = builder.build();
        var ref = new FabricRegRef<>(Registry.registerForHolder(registry, getId(name), tab));
        elements.put(ResourceLocation.fromNamespaceAndPath(modId, name), ref);
        TabDisplayItemsGenerator.setRegisteredGenerators(ref.getKey(), generator);
        return ref;
    }

    private ResourceKey<CreativeModeTab> getId(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, name);
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
    }
}
