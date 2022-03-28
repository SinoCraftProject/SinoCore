package games.moegirl.sinocraft.sinocore.api.world;

import games.moegirl.sinocraft.sinocore.api.utility.Suppliers;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PlacedFeatureRegister {

    private boolean isRegistered = false;
    private final List<Entry<?, ?>> features = new ArrayList<>();
    private final String modid;

    public PlacedFeatureRegister(String modid) {
        this.modid = modid;
    }

    public void register() {
        if (!isRegistered) {
            MinecraftForge.EVENT_BUS.register(this);
            isRegistered = true;
        }
    }

    public <C extends FeatureConfiguration, B extends BaseFeatureBuilder<C, B>> Entry<C, B>
    register(GenerationStep.Decoration decoration, String name, Filter filter, Supplier<B> feature) {
        Entry<C, B> e = new Entry<>(feature, filter, new ResourceLocation(modid, name), decoration);
        features.add(e);
        return e;
    }

    public Entry<OreConfiguration, OreFeatureBuilder> registerOre(String name, Filter filter, Supplier<OreFeatureBuilder> feature) {
        return register(GenerationStep.Decoration.UNDERGROUND_DECORATION, name, filter, feature);
    }

    public Entry<OreConfiguration, OreFeatureBuilder> registerOre(String name, Filter filter, Function<OreFeatureBuilder, OreFeatureBuilder> feature) {
        return registerOre(name, filter, Suppliers.decorate(OreFeatureBuilder::new, feature));
    }

    public Entry<OreConfiguration, OreFeatureBuilder> registerOre(String name, Function<OreFeatureBuilder, OreFeatureBuilder> feature) {
        return registerOre(name, Filter.ALL, feature);
    }

    public Entry<TreeConfiguration, TreeFeatureBuilder> registerTree(String name, Filter filter, Supplier<TreeFeatureBuilder> feature) {
        return register(GenerationStep.Decoration.VEGETAL_DECORATION, name, filter, feature);
    }

    public Entry<TreeConfiguration, TreeFeatureBuilder> registerTree(String name, Filter filter, Function<TreeFeatureBuilder, TreeFeatureBuilder> feature) {
        return registerTree(name, filter, Suppliers.decorate(TreeFeatureBuilder::new, feature));
    }

    public Entry<TreeConfiguration, TreeFeatureBuilder> registerTree(String name, Function<TreeFeatureBuilder, TreeFeatureBuilder> feature) {
        return registerTree(name, Filter.ALL, feature);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onGenerator(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        ResourceLocation name = event.getName();
        Biome.BiomeCategory category = event.getCategory();
        Biome.ClimateSettings climate = event.getClimate();
        BiomeSpecialEffects effects = event.getEffects();
        features.stream()
                .filter(e -> e.test(name, category, climate, effects))
                .forEach(e -> generation.addFeature(e.decoration(), Holder.direct(e.get())));
    }

    public static final class Entry<C extends FeatureConfiguration, B extends BaseFeatureBuilder<C, B>> implements Supplier<PlacedFeature> {
        private final Supplier<B> supplier;
        private final Filter filter;
        private final ResourceLocation name;
        private final GenerationStep.Decoration decoration;

        private B builder = null;

        public Entry(Supplier<B> supplier,
                     Filter filter, ResourceLocation name,
                     GenerationStep.Decoration decoration) {
            this.supplier = supplier;
            this.filter = filter;
            this.name = name;
            this.decoration = decoration;
        }

        boolean test(ResourceLocation biomeName, Biome.BiomeCategory category, Biome.ClimateSettings climate, BiomeSpecialEffects effects) {
            return filter.test(biomeName, category, climate, effects);
        }

        @Override
        public PlacedFeature get() {
            return getBuilder().build(name);
        }

        public GenerationStep.Decoration decoration() {
            return decoration;
        }

        public B getBuilder() {
            if (builder == null) {
                builder = supplier.get();
            }
            return builder;
        }
    }

    public interface Filter {
        Filter ALL = (biomeName, category, climate, effects) -> true;

        static Filter ofName(ResourceLocation biome) {
            return (biomeName, category, climate, effects) -> biome.equals(biomeName);
        }

        static Filter ofNames(ResourceLocation... biomes) {
            return ofNames(List.of(biomes));
        }

        static Filter ofNames(Collection<ResourceLocation> biomes) {
            return (biomeName, category, climate, effects) -> biomes.stream()
                    .anyMatch(rl -> Objects.equals(rl, biomeName));
        }

        static Filter ofBiome(Biome biome) {
            return (biomeName, category, climate, effects) -> biome.delegate.name().equals(biomeName);
        }

        static Filter ofBiomes(Biome... biomes) {
            return ofBiomes(List.of(biomes));
        }

        static Filter ofBiomes(Collection<Biome> biomes) {
            return (biomeName, category, climate, effects) -> biomes.stream()
                    .map(b -> b.delegate.name())
                    .anyMatch(rl -> Objects.equals(rl, biomeName));
        }

        static Filter ofCategory(Biome.BiomeCategory category) {
            return (biomeName, category2, climate, effects) -> category == category2;
        }

        static Filter ofCategories(Biome.BiomeCategory... categories) {
            return (biomeName, category, climate, effects) -> ArrayUtils.contains(categories, category);
        }

        static Filter ofCategories(Collection<Biome.BiomeCategory> categories) {
            return (biomeName, category, climate, effects) -> categories.contains(category);
        }

        static Filter ofClimate(Predicate<Biome.ClimateSettings> settings) {
            return (biomeName, category, climate, effects) -> settings.test(climate);
        }

        static Filter ofEffects(Predicate<BiomeSpecialEffects> effects) {
            return (biomeName, category, climate, effects2) -> effects.test(effects2);
        }

        boolean test(ResourceLocation biomeName, Biome.BiomeCategory category, Biome.ClimateSettings climate, BiomeSpecialEffects effects);
    }
}
