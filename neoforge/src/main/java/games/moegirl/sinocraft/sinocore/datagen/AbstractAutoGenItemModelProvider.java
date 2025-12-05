package games.moegirl.sinocraft.sinocore.datagen;

import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import games.moegirl.sinocraft.sinocore.datagen.model.LenientItemModelBuilder;
import games.moegirl.sinocraft.sinocore.datagen.model.UncheckedExistingModelFile;
import games.moegirl.sinocraft.sinocore.registry.IRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class AbstractAutoGenItemModelProvider extends ItemModelProvider {
    private final Logger logger;
    private final boolean strict;

    private final List<IRegistry<? extends Item>> registries;
    private final List<ResourceLocation> skipped = new ArrayList<>();

    @SafeVarargs
    public AbstractAutoGenItemModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper,
                                            IRegistry<? extends Item>... autoGenRegistries) {
        this(output, modId, existingFileHelper, false, autoGenRegistries);
    }

    @SafeVarargs
    public AbstractAutoGenItemModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper,
                                            boolean strict, IRegistry<? extends Item>... autoGenRegistries) {
        super(output, modId, existingFileHelper);
        this.registries = Arrays.asList(autoGenRegistries);
        this.logger = LoggerFactory.getLogger(getName());
        this.strict = strict;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        clear();
        registerModels();

        registries.stream()
                .flatMap(registry -> Streams.stream(registry.getEntries()))
                .filter(element -> !generatedModels.containsKey(element.getId()))
                .filter(element -> !skipped.contains(element.getId()))
                .map(Supplier::get)
                .map(Item::asItem)
                .forEach(this::autoGenItem);

        return generateAll(output);
    }

    @Override
    public ItemModelBuilder getBuilder(String path) {
        if (strict) {
            var builder = super.getBuilder(path);
            skipped.add(builder.getUncheckedLocation());
            return builder;
        }

        Preconditions.checkNotNull(path, "Path must not be null");
        var loc = locWithFolder(path.contains(":") ? mcLoc(path) : modLoc(path));
        skipped.add(loc);
        this.existingFileHelper.trackGenerated(loc, MODEL);
        return generatedModels.computeIfAbsent(loc, l -> new LenientItemModelBuilder(l, existingFileHelper, p -> logger.warn("Texture {} does not exist in any known resource pack", p)));
    }

    @Override
    public ModelFile.ExistingModelFile getExistingFile(ResourceLocation path) {
        if (strict) {
            return super.getExistingFile(path);
        }

        return new UncheckedExistingModelFile(path, existingFileHelper, p -> logger.warn("ModelFile {} does not exist in any known resource pack", p));
    }

    public ResourceLocation locWithFolder(ResourceLocation path) {
        return path.getPath().contains("/") ? path :
                ResourceLocation.fromNamespaceAndPath(path.getNamespace(), folder + "/" + path.getPath());
    }

    protected void skipAutoGen(Item... items) {
        skipped.addAll(Arrays.stream(items).map(BuiltInRegistries.ITEM::getKey).toList());
    }

    private void autoGenItem(Item item) {
        if (item instanceof BlockItem b) {
            var blockModel = modLoc(BLOCK_FOLDER + "/" + BuiltInRegistries.BLOCK.getKey(b.getBlock()).getPath());
            if (existingFileHelper.exists(blockModel, MODEL)) {
                simpleBlockItem(b.getBlock());
                return;
            }
        }

        if (item instanceof TieredItem) {
            handheldItem(item);
            return;
        }

        basicItem(item);
    }
}
