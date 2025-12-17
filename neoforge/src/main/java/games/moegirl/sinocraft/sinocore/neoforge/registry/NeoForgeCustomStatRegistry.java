package games.moegirl.sinocraft.sinocore.neoforge.registry;

import com.google.common.base.Suppliers;
import games.moegirl.sinocraft.sinocore.api.registry.ICustomStatRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgeCustomStatRegistry extends NeoForgeRegistry<ResourceLocation> implements ICustomStatRegistry {
    protected final Map<ResourceLocation, StatFormatter> formatters = new HashMap<>();

    public NeoForgeCustomStatRegistry(String modId) {
        super(modId, Registries.CUSTOM_STAT);
    }

    @Override
    public void register() {
        super.register();
        bus.addListener((RegisterEvent event) -> {
            for (Map.Entry<ResourceLocation, StatFormatter> entry : formatters.entrySet()) {
                Stats.CUSTOM.get(entry.getKey(), entry.getValue());
            }
        });
    }

    @Override
    public <R extends ResourceLocation> IRegRef<R> register(String name, Supplier<? extends R> supplier) {
        throw new AssertionError();
    }

    @Override
    public ResourceLocation register(String name, StatFormatter statFormatter) {
        ResourceLocation key = createId(name);
        register(name, Suppliers.ofInstance(key));
        formatters.put(key, statFormatter);
        return key;
    }
}
