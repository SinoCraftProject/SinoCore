package games.moegirl.sinocraft.sinocore.fabric.registry;

import com.google.common.base.Suppliers;
import games.moegirl.sinocraft.sinocore.api.registry.ICustomStatRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.IRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.fabric.RegistryManagerImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FabricCustomStatRegistry implements ICustomStatRegistry {

    private final IRegistry<ResourceLocation> reg;

    public FabricCustomStatRegistry(String modId) {
        reg = RegistryManagerImpl._create(modId, Registries.CUSTOM_STAT);
    }

    @Override
    public String modId() {
        return reg.modId();
    }

    @Override
    public void register() {
        reg.register();
    }

    @Override
    public ResourceLocation register(String name, StatFormatter statFormatter) {
        ResourceLocation statKey = ResourceLocation.fromNamespaceAndPath(modId(), name);
        reg.register(name, Suppliers.ofInstance(statKey));
        Stats.CUSTOM.get(statKey, statFormatter);
        return statKey;
    }

    @Override
    public @NotNull Registry<ResourceLocation> getRegistry() {
        return reg.getRegistry();
    }

    @Override
    public Iterable<IRegRef<ResourceLocation>> getEntries() {
        return reg.getEntries();
    }

    @Override
    public Optional<IRegRef<ResourceLocation>> get(ResourceLocation id) {
        return reg.get(id);
    }
}
