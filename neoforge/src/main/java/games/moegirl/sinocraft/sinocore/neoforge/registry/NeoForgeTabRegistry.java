package games.moegirl.sinocraft.sinocore.neoforge.registry;

import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.ITabRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NeoForgeTabRegistry extends NeoForgeRegistry<CreativeModeTab> implements ITabRegistry {

    public NeoForgeTabRegistry(String modId) {
        super(modId, Registries.CREATIVE_MODE_TAB);
    }

    @Override
    public <R extends CreativeModeTab> IRegRef<R> register(String name, Supplier<? extends R> supplier) {
        throw new AssertionError();
    }

    @Override
    public IRegRef<CreativeModeTab> registerTab(String name, Consumer<CreativeModeTab.Builder> consumer) {
        var ref = new NeoForgeRegRef<>(dr.register(name, () -> {
            var builder = CreativeModeTab.builder();
            consumer.accept(builder);
            return builder.build();
        }));
        elements.put(createId(name), ref);
        return ref;
    }
}
