package games.moegirl.sinocraft.sinocore.fabric.registry;

import com.google.common.base.Suppliers;
import games.moegirl.sinocraft.sinocore.api.registry.ICustomStatRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

import java.util.function.Supplier;

public class FabricCustomStatRegistry extends FabricRegistry<ResourceLocation> implements ICustomStatRegistry {

    public FabricCustomStatRegistry(String modId) {
        super(modId, Registries.CUSTOM_STAT);
    }

    @Override
    public <R extends ResourceLocation> IRegRef<R> register(String name, Supplier<? extends R> supplier) {
        throw new AssertionError();
    }

    @Override
    public ResourceLocation register(String name, StatFormatter statFormatter) {
        var key = createId(name);
        register(name, Suppliers.ofInstance(key));
        Stats.CUSTOM.get(key, statFormatter);
        return key;
    }
}
