package games.moegirl.sinocraft.sinocore.api.data;

import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.registry.RegistryManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class DataTypes {
    public static final ResourceKey<Registry<DataType<?>>> KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SinoCore.MODID, "data_type"));
    public static final Registry<DataType<?>> REGISTRY = RegistryManager.getOrCreateRegistry(KEY);

    public static void register() {
        // just for init
    }

    public static boolean has(ResourceLocation id) {
        return REGISTRY.containsKey(id);
    }

    @Nullable
    public static DataType<?> get(ResourceLocation id) {
        return REGISTRY.get(id);
    }
}
