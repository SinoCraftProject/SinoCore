package games.moegirl.sinocraft.sinocore.neoforge.api.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class AbstractLootTableProvider extends LootTableProvider {
    private final List<SubProviderEntry> subProviders = new ArrayList<>();

    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;

    protected final String modId;

    public AbstractLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        super(output, Set.of(), List.of(), registries);
        this.output = output;
        this.registries = registries;
        this.modId = modId;
    }

    // <editor-fold desc="Inner implementation">

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return createProvider().run(output);
    }

    private LootTableProvider createProvider() {
        register();
        return new LootTableProvider(output, Set.of(), subProviders, registries);
    }

    // </editor-fold>

    protected abstract void register();

    public void addProvider(Function<HolderLookup.Provider, LootTableSubProvider> provider, LootContextParamSet paramSet) {
        subProviders.add(new SubProviderEntry(provider, paramSet));
    }

    public abstract static class AbstractBlockLootSubProvider extends BlockLootSubProvider {
        public AbstractBlockLootSubProvider(HolderLookup.Provider registries) {
            this(FeatureFlags.REGISTRY.allFlags(), registries);
        }

        public AbstractBlockLootSubProvider(FeatureFlagSet enabledFeatures, HolderLookup.Provider registries) {
            this(Set.of(), enabledFeatures, registries);
        }

        public AbstractBlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, HolderLookup.Provider registries) {
            super(explosionResistant, enabledFeatures, registries);
        }
    }

    public abstract static class AbstractEntityLootSubProvider extends EntityLootSubProvider {
        public AbstractEntityLootSubProvider(HolderLookup.Provider registries) {
            this(FeatureFlags.REGISTRY.allFlags(), registries);
        }

        public AbstractEntityLootSubProvider(FeatureFlagSet required, HolderLookup.Provider registries) {
            super(required, registries);
        }
    }
}
