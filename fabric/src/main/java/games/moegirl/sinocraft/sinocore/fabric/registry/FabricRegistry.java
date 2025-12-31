package games.moegirl.sinocraft.sinocore.fabric.registry;

import com.google.common.base.Suppliers;
import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.IRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.RegistryManager;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Supplier;

public class FabricRegistry<T> implements IRegistry<T> {

    protected final String modId;
    protected final ResourceKey<Registry<T>> key;
    protected Registry<T> registry;
    protected final Supplier<Registry<T>> sup;
    protected final Map<ResourceLocation, IRegRef<T>> elements = new HashMap<>();

    @SuppressWarnings("unchecked")
    public FabricRegistry(String modId, ResourceKey<Registry<T>> key) {
        this.modId = modId;
        this.key = key;

        registry = RegistryManager.getOrCreateRegistry(key);
        sup = Suppliers.memoize(() -> (Registry<T>) BuiltInRegistries.REGISTRY.get(key.location()));
    }

    @Override
    public String modId() {
        return modId;
    }

    @Override
    public void register() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends T> IRegRef<R> register(String name, Supplier<? extends R> supplier) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, name);
        ResourceKey<T> eKey = ResourceKey.create(key, id);
        FabricRegRef<T> ref = new FabricRegRef<>(Registry.registerForHolder(registry, eKey, supplier.get()));
        elements.putIfAbsent(ResourceLocation.fromNamespaceAndPath(modId, name), ref);
        return (IRegRef<R>) ref;
    }

    @Override
    public Registry<T> getRegistry() {
        return sup.get();
    }

    @Override
    public Iterable<IRegRef<T>> getEntries() {
        return elements.values();
    }

    @Override
    public Optional<IRegRef<T>> get(ResourceLocation id) {
        return Optional.ofNullable(elements.get(id));
    }

    ResourceLocation createId(String name) {
        return ResourceLocation.fromNamespaceAndPath(modId, name);
    }
}
