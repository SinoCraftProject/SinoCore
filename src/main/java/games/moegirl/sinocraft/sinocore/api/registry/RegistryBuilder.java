package games.moegirl.sinocraft.sinocore.api.registry;

import lombok.Getter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * A builder for creating custom registries.
 *
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

    /**
     * Set the registry to be synchronized to client.
     *
     * @return this
     */
    public RegistryBuilder<T> sync() {
        return sync(true);
    }

    /**
     * Set whether the registry should be synchronized to client.
     *
     * @param value Whether to sync.
     * @return this
     */
    public RegistryBuilder<T> sync(boolean value) {
        this.sync = value;
        return this;
    }
}
