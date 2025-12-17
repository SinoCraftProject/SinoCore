package games.moegirl.sinocraft.sinocore.neoforge.registry;

import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.ITabRegistry;
import games.moegirl.sinocraft.sinocore.neoforge.util.ModBusHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class NeoForgeTabRegistry implements ITabRegistry {

    private final String modId;
    private final IEventBus bus;
    private final DeferredRegister<CreativeModeTab> dr;
    private final Map<ResourceLocation, IRegRef<CreativeModeTab>> elements = new HashMap<>();

    public NeoForgeTabRegistry(String modId) {
        this.modId = modId;
        this.bus = ModBusHelper.getModBus(modId);
        this.dr = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
    }

    @Override
    public String modId() {
        return modId;
    }

    @Override
    public void register() {
        dr.register(bus);
    }

    @Override
    public Registry<CreativeModeTab> getRegistry() {
        return dr.getRegistry().get();
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
        var ref = new NeoForgeRegRef<>(dr.register(name, () -> {
            var builder = CreativeModeTab.builder();
            consumer.accept(builder);
            return builder.build();
        }));
        elements.put(ResourceLocation.fromNamespaceAndPath(modId, name), ref);
        return ref;
    }
}
