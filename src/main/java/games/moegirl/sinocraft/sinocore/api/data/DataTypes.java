package games.moegirl.sinocraft.sinocore.api.data;

import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.registry.RegistryManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class DataTypes {
    public static final ResourceKey<Registry<DataType<?>>> KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SinoCore.MODID, "data_type"));
    public static final Registry<DataType<?>> REGISTRY = RegistryManager.getOrCreateRegistry(KEY);

    public static void register() {
        // just for init
    }

    public static Optional<DataType<?>> get(DataKey key) {
        return REGISTRY.stream().filter(type -> type.is(key)).findFirst();
    }
}
