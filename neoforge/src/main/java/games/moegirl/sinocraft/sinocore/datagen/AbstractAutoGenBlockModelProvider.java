package games.moegirl.sinocraft.sinocore.datagen;

import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import games.moegirl.sinocraft.sinocore.datagen.model.LenientBlockModelBuilder;
import games.moegirl.sinocraft.sinocore.datagen.model.UncheckedExistingModelFile;
import games.moegirl.sinocraft.sinocore.registry.IRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class AbstractAutoGenBlockModelProvider extends BlockModelProvider {
    private final Logger logger;
    private final boolean strict;

    private final List<IRegistry<? extends Block>> registries;
    private final List<ResourceLocation> skipped = new ArrayList<>();

    @SafeVarargs
    public AbstractAutoGenBlockModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper,
                                             IRegistry<? extends Block>... autoGenRegistries) {
        this(output, modId, existingFileHelper, false, autoGenRegistries);
    }

    @SafeVarargs
    public AbstractAutoGenBlockModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper,
                                             boolean strict, IRegistry<? extends Block>... autoGenRegistries) {
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
                .flatMap(reg -> Streams.stream(reg.getEntries()))
                .filter(element -> !generatedModels.containsKey(element.getId()))
                .filter(element -> !skipped.contains(element.getId()))
                .map(Supplier::get)
                .map(block -> (Block) block)
                .forEach(this::autoGenBlock);

        return generateAll(output);
    }

    @Override
    public BlockModelBuilder getBuilder(String path) {
        if (strict) {
            var builder = super.getBuilder(path);
            skipped.add(builder.getUncheckedLocation());
            return builder;
        }

        Preconditions.checkNotNull(path, "Path must not be null");
        var loc = locWithFolder(path.contains(":") ? mcLoc(path) : modLoc(path));
        skipped.add(loc);
        this.existingFileHelper.trackGenerated(loc, MODEL);
        return generatedModels.computeIfAbsent(loc, l -> new LenientBlockModelBuilder(l, existingFileHelper, p -> logger.warn("Texture {} does not exist in any known resource pack", p)));
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

    protected void skipAutoGen(Block... blocks) {
        skipped.addAll(Arrays.stream(blocks).map(BuiltInRegistries.BLOCK::getKey).toList());
    }

    protected void simpleBlock(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ResourceLocation texture = modLoc(BLOCK_FOLDER+ "/" + name);
        cubeAll(name, texture);
    }

    private void autoGenBlock(Block block) {
        simpleBlock(block);
    }
}
