package games.moegirl.sinocraft.sinocore.datagen;

import com.google.common.collect.Streams;
import games.moegirl.sinocraft.sinocore.registry.IRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class AbstractAutoGenItemModelProvider extends ItemModelProvider {

    private final List<IRegistry<? extends Item>> registries;
    private final List<ResourceLocation> skipped = new ArrayList<>();

    @SafeVarargs
    public AbstractAutoGenItemModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper,
                                            IRegistry<? extends Item>... autoGenRegistries) {
        super(output, modId, existingFileHelper);
        this.registries = Arrays.asList(autoGenRegistries);
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
        // Todo: weak existing file.

        return generateAll(output);
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
