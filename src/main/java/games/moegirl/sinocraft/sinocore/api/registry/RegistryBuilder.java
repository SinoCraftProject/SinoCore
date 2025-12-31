package games.moegirl.sinocraft.sinocore.api.registry;

import games.moegirl.sinocraft.sinocore.platform.RegistryPlatform;
import lombok.Getter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * A builder for creating custom registries.
 * @param <T> Registry entry type.
 * @see RegistryManager#createRegistry(RegistryBuilder)
 */
@Getter
public class RegistryBuilder<T> {
    private final ResourceKey<Registry<T>> key;

    private boolean sync = false;

    public RegistryBuilder(ResourceKey<Registry<T>> key) {
        this.key = key;
    }

    public RegistryBuilder<T> sync() {
        return sync(true);
    }

    public RegistryBuilder<T> sync(boolean value) {
        this.sync = value;
        return this;
    }
}
