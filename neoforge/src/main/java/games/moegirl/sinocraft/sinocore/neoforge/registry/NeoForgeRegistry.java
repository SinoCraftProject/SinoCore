package games.moegirl.sinocraft.sinocore.neoforge.registry;

import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.IRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.RegistryManager;
import games.moegirl.sinocraft.sinocore.neoforge.util.ModBusHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Supplier;

public class NeoForgeRegistry<T> implements IRegistry<T> {

    protected final IEventBus bus;
    protected final String modId;
    protected final Map<ResourceLocation, IRegRef<T>> elements = new HashMap<>();
    protected DeferredRegister<T> dr;
    protected boolean registered;

    public NeoForgeRegistry(String modId, ResourceKey<Registry<T>> key) {
        this.bus = ModBusHelper.getModBus(modId);

        this.modId = modId;
        this.dr = DeferredRegister.create(key, modId);

        if (!RegistryManager.hasRegistry(key)) {
            RegistryManager.createRegistry(key);
        }

        registered = false;
    }

    @Override
    public String modId() {
        return modId;
    }

    @Override
    public void register() {
        if (!registered) {
            dr.register(bus);
            registered = true;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends T> IRegRef<R> register(String name, Supplier<? extends R> supplier) {
        NeoForgeRegRef<T> ref = new NeoForgeRegRef<>(dr.register(name, supplier));
        elements.put(ResourceLocation.fromNamespaceAndPath(modId, name), ref);
        return (IRegRef<R>) ref;
    }

    @Override
    public Registry<T> getRegistry() {
        return dr.getRegistry().get();
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
