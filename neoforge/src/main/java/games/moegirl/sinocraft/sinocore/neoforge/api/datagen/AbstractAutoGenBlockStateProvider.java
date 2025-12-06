package games.moegirl.sinocraft.sinocore.neoforge.api.datagen;

import com.google.common.collect.Streams;
import games.moegirl.sinocraft.sinocore.registry.IRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class AbstractAutoGenBlockStateProvider extends BlockStateProvider {
    private final Logger logger;
    private final boolean strict;

    private final List<IRegistry<? extends Block>> registries = new ArrayList<>();
    private final List<ResourceLocation> skipped = new ArrayList<>();

    private final AbstractAutoGenBlockModelProvider blockModels;
    private final AbstractAutoGenItemModelProvider itemModels;

    public AbstractAutoGenBlockStateProvider(PackOutput output, String modId, ExistingFileHelper exFileHelper,
                                             List<IRegistry<? extends Block>> autoGenRegistries) {
        this(output, modId, exFileHelper, false, autoGenRegistries);
    }

    public AbstractAutoGenBlockStateProvider(PackOutput output, String modId, ExistingFileHelper exFileHelper,
                                             boolean strict, List<IRegistry<? extends Block>> autoGenRegistries) {
        super(output, modId, exFileHelper);

        var name = getName();
        this.logger = LoggerFactory.getLogger(name);
        this.strict = strict;
        this.registries.addAll(autoGenRegistries);

        this.blockModels = new AbstractAutoGenBlockModelProvider(output, modId, exFileHelper, strict, List.of()) {
            @Override
            public CompletableFuture<?> run(CachedOutput output) {
                return CompletableFuture.allOf();
            }

            @Override
            protected void register() {
            }

            @Override
            public String getName() {
                return "Inner BlockModels of: " + name;
            }
        };

        this.itemModels = new AbstractAutoGenItemModelProvider(output, modId, exFileHelper, strict, List.of()) {
            @Override
            public CompletableFuture<?> run(CachedOutput output) {
                return CompletableFuture.allOf();
            }

            @Override
            protected void register() {
            }

            @Override
            public String getName() {
                return "Inner ItemModels of: " + name;
            }
        };
    }

    // <editor-fold desc="Inner implementation">

    @Override
    protected final void registerStatesAndModels() {
        register();

        registries.stream()
                .flatMap(reg -> Streams.stream(reg.getEntries()))
                .filter(element -> !registeredBlocks.containsKey(element.get()))
                .filter(element -> !skipped.contains(element.getId()))
                .map(Supplier::get)
                .map(block -> (Block) block)
                .forEach(this::autoGenBlock);
    }

    @Override
    public AbstractAutoGenBlockModelProvider models() {
        return blockModels;
    }

    @Override
    public AbstractAutoGenItemModelProvider itemModels() {
        return itemModels;
    }

    private void autoGenBlock(Block block) {
        simpleBlockWithItem(block);
    }

    // </editor-fold>

    protected abstract void register();

    public void skipAutoGen(Block... blocks) {
        skipped.addAll(Arrays.stream(blocks).map(this::key).toList());
    }

    public ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public void simpleBlockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }
}
