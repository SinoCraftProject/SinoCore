package games.moegirl.sinocraft.sinocore.api.tree;

import com.mojang.datafixers.util.Pair;
import games.moegirl.sinocraft.sinocore.api.data.LanguageProviderBase;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Register trees
 */
@SuppressWarnings("JavadocReference")
public class TreeRegister {

    private final Map<ResourceLocation, TreeWithLanguage> trees = new HashMap<>();

    private final CreativeModeTab tab;
    private final DeferredRegister<Item> itemRegister;
    private final DeferredRegister<Block> blockRegister;

    public TreeRegister(CreativeModeTab tab, DeferredRegister<Item> itemRegister, DeferredRegister<Block> blockRegister) {
        this.tab = tab;
        this.itemRegister = itemRegister;
        this.blockRegister = blockRegister;
    }

    private void forEach(Consumer<Tree> treeConsumer) {
        trees.values().forEach(t -> treeConsumer.accept(t.tree));
    }

    /**
     * Register tree items, blocks, renders, etc to minecraft/forge
     *
     * @param bus           event bus
     * @param tab           items tab
     * @param itemRegister  register to register items
     * @param blockRegister register to register blocks
     */
    public void register(IEventBus bus) {
        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);
        if (FMLEnvironment.dist.isClient()) {
            bus.register(new ClientEvent());
        }
    }

    /**
     * Add a tree
     *
     * @param tree tree
     * @return this tree
     */
    public Tree register(Tree tree) {
        return register(null, null, tree);
    }

    /**
     * Add a tree with language
     *
     * @param chinese chinese name
     * @param tree    tree
     * @return this tree
     */
    public Tree register(@Nullable String chinese, Tree tree) {
        return register(null, chinese, tree);
    }

    /**
     * Add a tree with language
     *
     * @param english english name
     * @param chinese chinese name
     * @param tree    tree
     * @return this tree
     */
    public Tree register(@Nullable String english, @Nullable String chinese, Tree tree) {
        trees.put(tree.name, new TreeWithLanguage(tree, chinese, english));
        tree.getRegister().registerBlocks(blockRegister);
        tree.getRegister().registerItems(itemRegister, tab);
        return tree;
    }

    /**
     * Set tree chinese name
     *
     * @param name    tree key
     * @param chinese chinese name
     */
    public void setLanguage(ResourceLocation name, String chinese) {
        if (trees.containsKey(name)) {
            trees.get(name).chinese = chinese;
        }
    }

    /**
     * Set tree name
     *
     * @param name    tree key
     * @param english english name
     * @param chinese chinese name
     */
    public void setLanguage(ResourceLocation name, String english, String chinese) {
        if (trees.containsKey(name)) {
            TreeWithLanguage tree = trees.get(name);
            tree.english = english;
            tree.chinese = chinese;
        }
    }

    /**
     * Add tree languages to data provider, call in {@link LanguageProviderBase#addTranslations()}
     *
     * @param provider language provider
     */
    public void addLanguages(LanguageProviderBase provider) {
        trees.values().forEach(tree -> {
            if (tree.chinese != null) {
                if (tree.english != null) {
                    tree.tree.getRegister().addLanguages(provider, tree.english, tree.chinese);
                } else {
                    tree.tree.getRegister().addLanguages(provider, tree.chinese);
                }
            }
        });
    }

    /**
     * Add block models to data provider, call in {@link BlockStateProvider#registerStatesAndModels()}
     *
     * @param provider language provider
     */
    public void addBlockModels(BlockStateProvider provider) {
        forEach(tree -> tree.getRegister().addBlockModels(provider));
    }

    /**
     * Add item models to data provider, call in {@link ItemModelProvider#registerModels()}
     *
     * @param provider language provider
     */
    public void addItemModels(ItemModelProvider provider) {
        forEach(tree -> tree.getRegister().addItemModels(provider));
    }

    /**
     * Add recipe to data provider, call in {@link net.minecraft.data.recipes.RecipeProvider#buildCraftingRecipes(Consumer)}
     *
     * @param consumer consumer to build
     */
    public void addRecipes(Consumer<FinishedRecipe> consumer) {
        forEach(tree -> tree.getRegister().addRecipes(consumer));
    }

    /**
     * Add block tags to data provider, call in {@link TagsProvider#addTags()}
     *
     * @param tag method to create tag appender, use {@link TagsProvider#tag(TagKey)}
     */
    public void addBlockTags(Function<TagKey<Block>, TagsProvider.TagAppender<Block>> tag) {
        forEach(tree -> tree.getRegister().addBlockTags(tag));
    }

    /**
     * Add item tags to data provider, call in {@link TagsProvider#addTags()}
     *
     * @param tag method to create tag appender, use {@link TagsProvider#tag(TagKey)}
     */
    public void addItemTags(Function<TagKey<Item>, TagsProvider.TagAppender<Item>> tag) {
        forEach(tree -> tree.getRegister().addItemTags(tag));
    }

    /**
     * Add loot table to data provider, call in {@link LootTableProvider#getTables()}
     *
     * @param consumer consumer to build
     * @return all tree loot tables
     */
    public Set<TreeBlockLoot> addLoots(Consumer<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> consumer) {
        Set<TreeBlockLoot> set = new HashSet<>();
        forEach(tree -> set.add(tree.getRegister().addLoots(consumer)));
        return set;
    }

    public Map<ResourceLocation, Tree> getAllTrees() {
        return trees.values().stream().collect(Collectors.toMap(t -> t.tree.name, t -> t.tree));
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        forEach(tree -> tree.getRegister().registerTileEntityModifiers());
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        forEach(tree -> tree.getRegister().registerRenderType());
    }

    @OnlyIn(Dist.CLIENT)
    class ClientEvent {

        @SubscribeEvent
        public void onLayerDefinitions(net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions event) {
            forEach(tree -> tree.getRegister().registerBoatLayer(event));
        }
    }

    static final class TreeWithLanguage {
        final Tree tree;
        @Nullable
        String chinese;
        @Nullable
        String english;

        public TreeWithLanguage(Tree tree, @Nullable String chinese, @Nullable String english) {
            this.tree = tree;
            this.chinese = chinese;
            this.english = english;
        }
    }
}
