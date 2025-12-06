package games.moegirl.sinocraft.sinocore.neoforge.api.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public abstract class AbstractCodecProvider implements DataProvider {
    private final PackOutput output;
    protected final CompletableFuture<HolderLookup.Provider> registries;
    protected final String modId;

    private final List<Entry<?>> entries = new ArrayList<>();

    private final Logger logger;

    public AbstractCodecProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        this.output = output;
        this.registries = registries;
        this.modId = modId;
        this.logger = LoggerFactory.getLogger(getName());
    }

    // <editor-fold desc="Inner implementation">

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        register();
        return registries.thenCompose(registry -> {
            RegistryOps<JsonElement> jsonOps = RegistryOps.create(JsonOps.INSTANCE, registry);
            return CompletableFuture.allOf(entries.stream()
                    .flatMap(entry -> entry.save(output, this.output, jsonOps))
                    .toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "Codec Provider: " + modId;
    }

    // </editor-fold>

    protected abstract void register();

    @SuppressWarnings("unchecked")
    public <T> Entry<T> getEntry(ResourceKey<Registry<T>> type, Codec<T> codec) {
        for (var entry : entries) {
            if (entry.type.equals(type)) {
                return (Entry<T>) entry;
            }
        }

        Entry<T> entry = new Entry<>(type, codec);
        entries.add(entry);
        return entry;
    }

    public void add(ResourceLocation name, ConfiguredFeature<?, ?> feature) {
        getEntry(Registries.CONFIGURED_FEATURE, ConfiguredFeature.DIRECT_CODEC).add(name, feature);
    }

    public <T> void add(ResourceKey<Registry<T>> type, Codec<T> codec, ResourceLocation name, T value) {
        getEntry(type, codec).add(name, value);
    }

    public ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }

    public class Entry<T> {
        private final ResourceKey<Registry<T>> type;
        private final Codec<T> codec;

        private final Map<ResourceLocation, T> items = new HashMap<>();

        Entry(ResourceKey<Registry<T>> type, Codec<T> codec) {
            this.type = type;
            this.codec = codec;
        }

        public void add(ResourceLocation id, T value) {
            items.put(id, value);
        }

        private Stream<CompletableFuture<?>> save(CachedOutput output, PackOutput packOutput, RegistryOps<JsonElement> jsonOps) {
            ResourceLocation key = type.location();
            String subPath = key.getNamespace() + "/" + key.getPath();
            PackOutput.PathProvider pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, subPath);
            return items.entrySet()
                    .stream()
                    .map(entry -> codec.encodeStart(jsonOps, entry.getValue())
                            .resultOrPartial(str -> AbstractCodecProvider.this.logger.error("Couldn't serialize element {}: {}", entry.getKey(), str))
                            .map(json -> DataProvider.saveStable(output, json, pathProvider.json(entry.getKey())))
                            .orElseGet(() -> CompletableFuture.completedFuture(null)));
        }
    }
}
