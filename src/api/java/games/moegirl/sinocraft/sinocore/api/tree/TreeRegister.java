package games.moegirl.sinocraft.sinocore.api.tree;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * * RegisterXxx methods means you must call in the mod, but addXxx is optional.
 */
@SuppressWarnings("JavadocReference")
public record TreeRegister(Tree tree) {

    /**
     * Register all block's render types, call in {@link FMLClientSetupEvent}
     */
    @OnlyIn(Dist.CLIENT)
    public void registerRenderType() {
        net.minecraft.client.renderer.RenderType cutoutMipped = net.minecraft.client.renderer.RenderType.cutoutMipped();
        net.minecraft.client.renderer.RenderType cutout = net.minecraft.client.renderer.RenderType.cutout();

        net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(tree.leaves(), cutoutMipped);
        net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(tree.sapling(), cutout);
        net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(tree.pottedSapling(), cutout);
    }

    /**
     * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, add chinese name
     *
     * @param provider language provider
     * @param chinese  tree chinese name
     */
    public void addLanguagesZh(LanguageProvider provider, String chinese) {
        provider.addBlock(tree.sapling, chinese + "树苗");
        provider.addBlock(tree.log, chinese + "原木");
        provider.addBlock(tree.strippedLog, "去皮" + chinese + "原木");
        provider.addBlock(tree.wood, chinese + "木");
        provider.addBlock(tree.strippedWoods, "去皮" + chinese + "木");
        provider.addBlock(tree.leaves, chinese + "树叶");
        provider.addBlock(tree.pottedSapling, chinese + "树苗盆栽");
    }

    /**
     * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
     *
     * @param provider language provider
     * @param english  tree english name
     */
    public void addLanguagesEn(LanguageProvider provider) {
        addLanguagesEn(provider, defaultEnglishName());
    }

    /**
     * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
     *
     * @param provider language provider
     */
    public void addLanguagesEn(LanguageProvider provider, String english) {
        provider.addBlock(tree.sapling, english + " Sapling");
        provider.addBlock(tree.log, english + " Log");
        provider.addBlock(tree.strippedLog, "Stripped" + english + " Stripped");
        provider.addBlock(tree.wood, english + " Wood");
        provider.addBlock(tree.strippedWoods, "Stripped " + english + " Wood");
        provider.addBlock(tree.leaves, english + " Leaves");
        provider.addBlock(tree.pottedSapling, "Potted " + english + " Sapling");
    }

    private String defaultEnglishName() {
        StringBuilder sb = new StringBuilder();
        for (String s : tree.name().getPath().split("_")) {
            if (s.isEmpty()) continue;
            String s1 = s.toLowerCase(Locale.ROOT);
            sb.append(Character.toUpperCase(s1.charAt(0)));
            sb.append(s1.substring(1));
        }
        return sb.toString();
    }

    /**
     * call this method in {@link BlockStateProvider#registerStatesAndModels} or other equivalent method.
     *
     * @param provider block model provider
     */
    public void addBlockModels(BlockStateProvider provider) {
        provider.simpleBlock(tree.sapling(),
                provider.models().cross(tree.sapling.getId().getPath(), provider.blockTexture(tree.sapling())));
        provider.logBlock(tree.log());
        provider.logBlock(tree.strippedLog());
        provider.simpleBlock(tree.wood(), provider.models().cubeColumn(tree.wood.getId().getPath(),
                provider.blockTexture(tree.log()),
                provider.blockTexture(tree.log())));
        provider.simpleBlock(tree.strippedWoods(), provider.models().cubeColumn(tree.strippedWoods.getId().getPath(),
                provider.blockTexture(tree.strippedLog()),
                provider.blockTexture(tree.strippedLog())));
        provider.simpleBlock(tree.leaves(), provider.models().singleTexture(
                tree.leaves.getId().getPath(),
                provider.mcLoc(ModelProvider.BLOCK_FOLDER + "/leaves"),
                "all", provider.blockTexture(tree.leaves())));
        provider.simpleBlock(tree.pottedSapling(), provider.models().singleTexture(
                tree.pottedSapling.getId().getPath(),
                provider.mcLoc(ModelProvider.BLOCK_FOLDER + "/flower_pot_cross"),
                "plant", provider.modLoc(ModelProvider.BLOCK_FOLDER + "/" + tree.sapling.getId().getPath())));
    }

    /**
     * call this method in {@link ItemModelProvider#registerModels} or other equivalent method.
     *
     * @param provider item model provider
     */
    public void addItemModels(ItemModelProvider provider) {
        addBlockItem(tree.sapling, provider);
        addBlockItem(tree.log, provider);
        addBlockItem(tree.strippedLog, provider);
        addBlockItem(tree.strippedWoods, provider);
        addBlockItem(tree.wood, provider);
        addBlockItem(tree.leaves, provider);
    }

    private void addBlockItem(RegistryObject<? extends Block> block, ItemModelProvider provider) {
        String path = block.getId().getPath();
        provider.withExistingParent(path, provider.modLoc("block/" + path));
    }

    /**
     * call this method in {@link RecipeProvider#buildCraftingRecipes}
     * or other equivalent method.
     *
     * @param consumer save consumer
     * @param plank plank name, if not null, will add recipe: log -> 4*planks
     */
    @SuppressWarnings("JavadocReference")
    public void addRecipes(Consumer<FinishedRecipe> consumer, @Nullable Block planks) {
        // planksFromLog
        if (planks != null) {
            InventoryChangeTrigger.TriggerInstance hasLogs = InventoryChangeTrigger.TriggerInstance
                    .hasItems(ItemPredicate.Builder.item().of(ItemTags.LOGS).build());
            ShapelessRecipeBuilder.shapeless(planks, 4).group("planks")
                    .requires(tree.log())
                    .unlockedBy("has_log", hasLogs)
                    .save(consumer);
        }

        InventoryChangeTrigger.TriggerInstance hasLog = InventoryChangeTrigger.TriggerInstance
                .hasItems(ItemPredicate.Builder.item().of(tree.log()).build());
        // woodFromLogs
        ShapedRecipeBuilder.shaped(tree.wood(), 3).group("bark")
                .define('#', tree.log())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_log", hasLog)
                .save(consumer);
        ShapedRecipeBuilder.shaped(tree.strippedWoods(), 3).group("bark")
                .define('#', tree.strippedLog())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_log", hasLog)
                .save(consumer);
    }

    /**
     * call this method in {@link TagsProvider#addTags} or other equivalent method.
     *
     * @param tag tag appender creator, use {@link TagsProvider#tag}
     */
    public void addBlockTags(Function<TagKey<Block>, TagsProvider.TagAppender<Block>> tag) {
        tag.apply(tree.tagLogs).add(tree.log(), tree.strippedLog(), tree.wood(), tree.strippedLog());
        tag.apply(BlockTags.SAPLINGS).add(tree.sapling());
        tag.apply(BlockTags.LOGS_THAT_BURN).addTag(tree.tagLogs);
        tag.apply(BlockTags.FLOWER_POTS).add(tree.pottedSapling());
        tag.apply(BlockTags.LEAVES).add(tree.leaves());
    }

    /**
     * call this method in {@link TagsProvider#addTags} or other equivalent method.
     *
     * @param tag tag appender creator, use {@link TagsProvider#tag}
     */
    public void addItemTags(Function<TagKey<Item>, TagsProvider.TagAppender<Item>> tag) {
        tag.apply(tree.tagItemLogs)
                .add(tree.log().asItem(), tree.strippedLog().asItem(), tree.wood().asItem(), tree.strippedLog().asItem());
        tag.apply(ItemTags.SAPLINGS).add(tree.sapling().asItem());
        tag.apply(ItemTags.LOGS_THAT_BURN).addTag(tree.tagItemLogs);
        tag.apply(ItemTags.LEAVES).add(tree.leaves().asItem());
    }

    /**
     * call this method in {@link LootTableProvider#getTables()} or other equivalent method.
     *
     * @param consumer consumer to collect table builder
     * @return block loot for add tree
     */
    public TreeBlockLoot addLoots(Consumer<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> consumer) {
        TreeBlockLoot loot = new TreeBlockLoot(tree);
        consumer.accept(Pair.of(() -> loot, LootContextParamSets.BLOCK));
        return loot;
    }
}
