package games.moegirl.sinocraft.sinocore.neoforge.api.datagen;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCodecProvider implements DataProvider {
    private final PackOutput output;
    protected final CompletableFuture<HolderLookup.Provider> registries;
    protected final String modId;

    protected Map<ResourceKey<?>, CodecSubProvider<?>> subProviders = new HashMap<>();

    public AbstractCodecProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        this.output = output;
        this.registries = registries;
        this.modId = modId;
    }

    // <editor-fold desc="Inner implementation">

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return registries.thenApply(registry -> {
            register(registry);
            return registry;
        }).thenCompose(registry -> CompletableFuture.allOf(subProviders.values()
                .stream()
                .map(codecSubProvider -> codecSubProvider.run(output))
                .toArray(CompletableFuture[]::new)));
    }

    @Override
    public String getName() {
        return "Codec Provider: " + modId;
    }

    // </editor-fold>

    protected abstract void register(HolderLookup.Provider registry);

    @SuppressWarnings("unchecked")
    public <T> CodecSubProvider<T> type(ResourceKey<Registry<T>> type) {
        if (!subProviders.containsKey(type)) {
            subProviders.put(type, new CodecSubProvider<>(output, registries, modId, type));
        }
        return (CodecSubProvider<T>) subProviders.get(type);
    }

    public <T> void add(ResourceKey<Registry<T>> type, Codec<T> codec, ResourceLocation name, T value) {
        type(type).add(name, codec, value);
    }

    public ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }
}
