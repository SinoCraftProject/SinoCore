package games.moegirl.sinocraft.sinocore.neoforge.api.datagen;

import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.advancement.AdvancementTree;
import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.advancement.DisplayInfoBuilder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractAdvancementProvider extends AdvancementProvider {
    private final List<AdvancementProvider.AdvancementGenerator> generators = new ArrayList<>();

    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final ExistingFileHelper existingFileHelper;

    protected String modId;

    public AbstractAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
                                       ExistingFileHelper existingFileHelper, String modId) {
        super(output, registries, existingFileHelper, List.of());
        this.output = output;
        this.registries = registries;
        this.existingFileHelper = existingFileHelper;
        this.modId = modId;
    }

    // <editor-fold desc="Inner implementation">

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return createProvider().run(output);
    }

    private AdvancementProvider createProvider() {
        register();
        return new AdvancementProvider(output, registries, existingFileHelper, generators);
    }

    // </editor-fold>

    protected abstract void register();

    protected void addAdvancements(AdvancementProvider.AdvancementGenerator generator) {
        generators.add(generator);
    }

    protected void addAdvancementTree(Function<Consumer<AdvancementHolder>, AdvancementTree> tree) {
        addAdvancements((registries, saver, context) -> tree.apply(saver));
    }

    protected Criterion<InventoryChangeTrigger.TriggerInstance> triggerGotItems(ItemLike... items) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(items);
    }

    protected Criterion<InventoryChangeTrigger.TriggerInstance> triggerGotItems(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag));
    }

    protected DisplayInfoBuilder display(ResourceLocation background) {
        return new DisplayInfoBuilder().setBackground(background);
    }

    protected ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }
}
