package games.moegirl.sinocraft.sinocore.neoforge.api.datagen;

import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.model.LenientItemModelBuilder;
import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.model.UncheckedExistingModelFile;
import games.moegirl.sinocraft.sinocore.api.registry.IRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractAutoGenItemModelProvider extends ItemModelProvider {
    public static final String GENERATED = "item/generated";
    public static final String HANDHELD = "item/handheld";

    private final Logger logger;
    private final boolean strict;

    private final List<IRegistry<? extends Item>> registries = new ArrayList<>();
    private final List<ResourceLocation> skipped = new ArrayList<>();

    public AbstractAutoGenItemModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper,
                                            List<IRegistry<? extends Item>> autoGenRegistries) {
        this(output, modId, existingFileHelper, false, autoGenRegistries);
    }

    public AbstractAutoGenItemModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper,
                                            boolean strict, List<IRegistry<? extends Item>> autoGenRegistries) {
        super(output, modId, existingFileHelper);
        this.logger = LoggerFactory.getLogger(getName());
        this.strict = strict;
        this.registries.addAll(autoGenRegistries);
    }

    // <editor-fold desc="Inner implementation">

    @Override
    protected final void registerModels() {
        register();

        registries.stream()
                .flatMap(registry -> Streams.stream(registry.getEntries()))
                .filter(element -> !generatedModels.containsKey(element.getId()))
                .filter(element -> !skipped.contains(element.getId()))
                .map(Supplier::get)
                .map(Item::asItem)
                .forEach(this::autoGenItem);
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
        return generatedModels.computeIfAbsent(loc, l -> new LenientItemModelBuilder(l, existingFileHelper, p -> logger.error("Texture {} does not exist in any known resource pack", p)));
    }

    @Override
    public ModelFile.ExistingModelFile getExistingFile(ResourceLocation path) {
        if (strict) {
            return super.getExistingFile(path);
        }

        return new UncheckedExistingModelFile(path, existingFileHelper, p -> logger.error("ModelFile {} does not exist in any known resource pack", p));
    }

    private void autoGenItem(Item item) {
        if (item instanceof BlockItem b) {
            var blockModel = modLoc(BLOCK_FOLDER + "/" + key(b.getBlock()).getPath());
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

    // </editor-fold>

    protected abstract void register();

    public ResourceLocation locWithFolder(ResourceLocation path) {
        return path.getPath().contains("/") ? path :
                ResourceLocation.fromNamespaceAndPath(path.getNamespace(), folder + "/" + path.getPath());
    }

    public void skipAutoGen(Item... items) {
        skipped.addAll(Arrays.stream(items).map(this::key).toList());
    }

    public ResourceLocation key(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public ItemModelBuilder basicItem(Item item, ResourceLocation texture) {
        return basicItem(Objects.requireNonNull(key(item)), texture);
    }

    public ItemModelBuilder basicItem(ResourceLocation item, ResourceLocation texture) {
        return withExistingParent(item.toString(), GENERATED)
                .texture("layer0", texture);
    }
}
