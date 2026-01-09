package games.moegirl.sinocraft.sinocore.neoforge.api.datagen;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CodecSubProvider<T> implements DataProvider {
    private final PackOutput output;
    protected final CompletableFuture<HolderLookup.Provider> registries;
    protected final String modId;

    protected final ResourceKey<Registry<T>> type;
    protected final Map<ResourceLocation, Entry<?>> entries = new HashMap<>();

    public CodecSubProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId, ResourceKey<Registry<T>> type) {
        this.output = output;
        this.registries = registries;
        this.modId = modId;
        this.type = type;
    }

    public void add(ResourceLocation name, Codec<T> codec, T value) {
        entries.put(name, new Entry<>(codec, value));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return registries.thenCompose(registry -> {
            ResourceLocation typeKey = type.location();
            PackOutput.PathProvider pathProvider = this.output.createPathProvider(PackOutput.Target.DATA_PACK, typeKey.getPath());
            return CompletableFuture.allOf(entries.entrySet()
                    .stream()
                    .map(entry ->
                            entry.getValue().save(output, registry, pathProvider.json(entry.getKey())))
                    .toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "Codec provider of type: " + type + " for mod:" + modId;
    }

    public static class Entry<T> {
        private final Codec<T> codec;
        private final T value;

        Entry(Codec<T> codec, T value) {
            this.codec = codec;
            this.value = value;
        }

        private CompletableFuture<?> save(CachedOutput output, HolderLookup.Provider registry, Path path) {
            return DataProvider.saveStable(output, registry, codec, value, path);
        }
    }
}
