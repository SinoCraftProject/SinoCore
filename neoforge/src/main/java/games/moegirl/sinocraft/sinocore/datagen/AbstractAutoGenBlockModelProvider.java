package games.moegirl.sinocraft.sinocore.datagen;

import com.google.common.collect.Streams;
import games.moegirl.sinocraft.sinocore.registry.IRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class AbstractAutoGenBlockModelProvider extends BlockModelProvider {
    private final List<IRegistry<? extends Block>> registries;
    private final List<ResourceLocation> skipped = new ArrayList<>();

    @SafeVarargs
    public AbstractAutoGenBlockModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper,
                                             IRegistry<? extends Block>... autoGenRegistries) {
        super(output, modId, existingFileHelper);
        this.registries = Arrays.asList(autoGenRegistries);
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
        // Todo: weak existing file.

        return generateAll(output);
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
