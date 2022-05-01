package games.moegirl.sinocraft.sinocore.api.woodwork;

import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record WoodworkRegisterHelper(Woodwork woodwork) {

    private static final Logger LOGGER = LogManager.getLogger();

    public void register(IEventBus bus) {
        WoodworkRegisterEvents events = new WoodworkRegisterEvents(this);
        bus.addListener(events::onSetup);
        bus.addListener(events::onClient);
        if (Environment.get().getDist().isClient()) {
            bus.register(events.client());
        }
    }

    /**
     * Call when {@link FMLClientSetupEvent} event fired
     */
    @OnlyIn(Dist.CLIENT)
    public void registerRender() {
        net.minecraft.client.renderer.RenderType cutout = net.minecraft.client.renderer.RenderType.cutout();
        net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(woodwork.trapdoor(), cutout);
        net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(woodwork.door(), cutout);
        net.minecraft.client.renderer.Sheets.addWoodType(woodwork.type);
    }

    /**
     * Call when {@link EntityRenderersEvent.RegisterLayerDefinitions} event fired
     */
    @OnlyIn(Dist.CLIENT)
    public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // sign
        // ModSignRenderer.registerLayer(woodwork, event);
    }

    /**
     * Call when {@link EntityRenderersEvent.RegisterRenderers} event fired
     */
    @OnlyIn(Dist.CLIENT)
    public void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
        if (woodwork.useDefaultSignEntity()) {
            event.registerBlockEntityRenderer(woodwork.signEntity(), ModSignRenderer::new);
        }
        if (woodwork.useDefaultWallSignEntity()) {
            event.registerBlockEntityRenderer(woodwork.wallSignEntity(), ModSignRenderer::new);
        }
    }

    // DataProvider ====================================================================================================

    /**
     * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, add chinese name
     *
     * @param provider language provider
     * @param chinese  tree chinese name
     */
    @SuppressWarnings("JavadocReference")
    public void addLanguagesZh(LanguageProvider provider, String chinese) {
        provider.addBlock(woodwork.planks, chinese + "木板");
        provider.addBlock(woodwork.sign, chinese + "木告示牌");
        provider.add(Util.makeDescriptionId("block", woodwork.wallSign.getId()), "墙上的" + chinese + "木告示牌");
        provider.addBlock(woodwork.pressurePlate, chinese + "木压力板");
        provider.addBlock(woodwork.trapdoor, chinese + "木活板门");
        provider.addBlock(woodwork.stairs, chinese + "木楼梯");
        provider.addBlock(woodwork.button, chinese + "木按钮");
        provider.addBlock(woodwork.slab, chinese + "木台阶");
        provider.addBlock(woodwork.fenceGate, chinese + "木栅栏门");
        provider.addBlock(woodwork.fence, chinese + "木栅栏");
        provider.addBlock(woodwork.door, chinese + "木门");
    }

    /**
     * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
     *
     * @param provider language provider
     */
    @SuppressWarnings("JavadocReference")
    public void addLanguagesEn(LanguageProvider provider) {
        addLanguagesEn(provider, defaultEnglishName());
    }

    /**
     * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
     *
     * @param provider language provider
     * @param english  english name
     */
    @SuppressWarnings("JavadocReference")
    public void addLanguagesEn(LanguageProvider provider, String english) {
        provider.addBlock(woodwork.planks, english + " Planks");
        provider.addBlock(woodwork.sign, english + " Sign");
        provider.add(Util.makeDescriptionId("block", woodwork.wallSign.getId()), english + " Wall Sign");
        provider.addBlock(woodwork.pressurePlate, english + " Pressure Plate");
        provider.addBlock(woodwork.trapdoor, english + " Trap Trapdoor");
        provider.addBlock(woodwork.stairs, english + " Stairs");
        provider.addBlock(woodwork.button, english + " Button");
        provider.addBlock(woodwork.slab, english + " Slab");
        provider.addBlock(woodwork.fenceGate, english + " Fence Gate");
        provider.addBlock(woodwork.fence, english + " Fence");
        provider.addBlock(woodwork.door, english + " Door");
    }

    private String defaultEnglishName() {
        return Arrays.stream(woodwork.name.getPath().split("_"))
                .filter(s -> !s.isEmpty())
                .map(s -> s.length() == 1 ? s.toUpperCase(Locale.ROOT) : Character.toUpperCase(s.charAt(0)) + s.substring(1))
                .collect(Collectors.joining(" "));
    }

    /**
     * call this method in {@link BlockStateProvider#registerStatesAndModels} or other equivalent method.
     *
     * @param provider block model provider
     */
    @SuppressWarnings("JavadocReference")
    public void addBlockModels(BlockStateProvider provider) {
        ExistingFileHelper helper = provider.models().existingFileHelper;
        ExistingFileHelper.ResourceType texType =
                new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

        ResourceLocation texPlank = provider.blockTexture(woodwork.planks());

        provider.simpleBlock(woodwork.planks());
        // sign block
        // provider.signBlock(woodwork.sign(), woodwork.wallSign(), texPlank);
        {
            ModelFile sign = provider.models().sign(woodwork.sign.getId().getPath(), texPlank);
            provider.simpleBlock(woodwork.sign(), sign);
            provider.simpleBlock(woodwork.wallSign(), sign);
        }
        provider.pressurePlateBlock(woodwork.pressurePlate(), texPlank);
        // orientable?
        provider.trapdoorBlock(woodwork.trapdoor(), texPlank, true);
        provider.stairsBlock(woodwork.stairs(), texPlank);
        // button
        {
            provider.models().singleTexture(woodwork.button.getId().getPath() + "_inventory",
                    provider.mcLoc(ModelProvider.BLOCK_FOLDER + "/button_inventory"), texPlank);
            provider.buttonBlock(woodwork.button(), texPlank);
        }
        provider.slabBlock(woodwork.slab(), texPlank, texPlank);
        provider.fenceGateBlock(woodwork.fenceGate(), texPlank);
        provider.fenceBlock(woodwork.fence(), texPlank);

        DoorBlock door = woodwork.door();
        ResourceLocation doorName = woodwork.door.getId();
        ResourceLocation doorTop = new ResourceLocation(doorName.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + doorName.getPath() + "_top");
        ResourceLocation doorBottom = new ResourceLocation(doorName.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + doorName.getPath() + "_bottom");
        if (!helper.exists(doorTop, texType)) {
            LOGGER.warn(doorTop + " not exist, use " + texPlank);
            doorTop = texPlank;
        }
        if (!helper.exists(doorBottom, texType)) {
            LOGGER.warn(doorBottom + " not exist, use " + texPlank);
            doorBottom = texPlank;
        }
        provider.doorBlock(door, doorBottom, doorTop);
        provider.models().fenceInventory(woodwork.fence.getId().getPath() + "_inventory", texPlank);
    }

    /**
     * call this method in {@link ItemModelProvider#registerModels} or other equivalent method.
     *
     * @param provider item model provider
     */
    @SuppressWarnings("JavadocReference")
    public void addItemModels(ItemModelProvider provider) {
        addBlockItem(woodwork.planks, provider);
        addBlockItem(woodwork.slab, provider);
        addBlockItem(woodwork.fence, "inventory", provider);
        addBlockItem(woodwork.stairs, provider);
        addBlockItem(woodwork.button, "inventory", provider);
        addBlockItem(woodwork.pressurePlate, provider);
        addBlockItem(woodwork.trapdoor, "bottom", provider);
        addBlockItem(woodwork.fenceGate, provider);

        addItem(woodwork.door, provider);
        addItem(woodwork.sign, provider);
    }

    private void addBlockItem(RegistryObject<? extends Block> block, ItemModelProvider provider) {
        String path = block.getId().getPath();
        provider.withExistingParent(path, provider.modLoc("block/" + path));
    }

    private void addBlockItem(RegistryObject<? extends Block> block, String postfix, ItemModelProvider provider) {
        String path = block.getId().getPath();
        provider.withExistingParent(path, provider.modLoc("block/" + path + "_" + postfix));
    }

    private void addItem(RegistryObject<? extends ItemLike> item, ItemModelProvider provider) {
        ResourceLocation name = item.get().asItem().delegate.name();
        provider.singleTexture(name.getPath(), provider.mcLoc("item/generated"),
                "layer0", provider.modLoc("item/" + name.getPath()));
    }

    /**
     * call this method in {@link RecipeProvider#buildCraftingRecipes}
     * or other equivalent method.
     *
     * @param consumer save consumer
     * @param log      log block, if not is null, add recipe: log -> planks * 4
     */
    @SuppressWarnings("JavadocReference")
    public void addRecipes(Consumer<FinishedRecipe> consumer, @Nullable Block log) {
        InventoryChangeTrigger.TriggerInstance hasLogs = InventoryChangeTrigger.TriggerInstance
                .hasItems(ItemPredicate.Builder.item().of(ItemTags.LOGS).build());
        InventoryChangeTrigger.TriggerInstance hasPlank = InventoryChangeTrigger.TriggerInstance
                .hasItems(ItemPredicate.Builder.item().of(woodwork.planks()).build());
        // planksFromLog
        if (log != null) {
            ShapelessRecipeBuilder.shapeless(woodwork.planks(), 4).group("planks")
                    .requires(log)
                    .unlockedBy("has_log", hasLogs)
                    .save(consumer);
        }

        BlockFamilyRegister.generateRecipes(consumer, new BlockFamily.Builder(woodwork.planks())
                .button(woodwork.button())
                .fence(woodwork.fence())
                .fenceGate(woodwork.fenceGate())
                .pressurePlate(woodwork.pressurePlate())
                .sign(woodwork.sign(), woodwork.wallSign())
                .slab(woodwork.slab())
                .stairs(woodwork.stairs())
                .door(woodwork.door())
                .trapdoor(woodwork.trapdoor())
                .recipeGroupPrefix("wooden")
                .recipeUnlockedBy("has_planks")
                .dontGenerateModel()
                .getFamily());
    }

    /**
     * call this method in {@link TagsProvider#addTags} or other equivalent method.
     *
     * @param tag tag provider
     */
    @SuppressWarnings("JavadocReference")
    public void addBlockTags(Function<TagKey<Block>, TagsProvider.TagAppender<Block>> tag) {
        tag.apply(BlockTags.PLANKS).add(woodwork.planks());
        tag.apply(BlockTags.WOODEN_BUTTONS).add(woodwork.button());
        tag.apply(BlockTags.WOODEN_DOORS).add(woodwork.door());
        tag.apply(BlockTags.WOODEN_STAIRS).add(woodwork.stairs());
        tag.apply(BlockTags.WOODEN_SLABS).add(woodwork.slab());
        tag.apply(BlockTags.WOODEN_FENCES).add(woodwork.fence());
        tag.apply(BlockTags.WOODEN_PRESSURE_PLATES).add(woodwork.pressurePlate());
        tag.apply(BlockTags.WOODEN_TRAPDOORS).add(woodwork.trapdoor());
        tag.apply(BlockTags.STANDING_SIGNS).add(woodwork.sign());
        tag.apply(BlockTags.WALL_SIGNS).add(woodwork.wallSign());
        tag.apply(BlockTags.FENCE_GATES).add(woodwork.fenceGate());
        tag.apply(Tags.Blocks.FENCE_GATES_WOODEN).add(woodwork.fenceGate());
    }

    /**
     * call this method in {@link TagsProvider#addTags} or other equivalent method.
     *
     * @param tag tag provider
     */
    @SuppressWarnings("JavadocReference")
    public void addItemTags(Function<TagKey<Item>, TagsProvider.TagAppender<Item>> tag) {
        tag.apply(ItemTags.PLANKS).add(woodwork.planks().asItem());
        tag.apply(ItemTags.WOODEN_BUTTONS).add(woodwork.button().asItem());
        tag.apply(ItemTags.WOODEN_DOORS).add(woodwork.door().asItem());
        tag.apply(ItemTags.WOODEN_STAIRS).add(woodwork.stairs().asItem());
        tag.apply(ItemTags.WOODEN_SLABS).add(woodwork.slab().asItem());
        tag.apply(ItemTags.WOODEN_FENCES).add(woodwork.fence().asItem());
        tag.apply(ItemTags.WOODEN_PRESSURE_PLATES).add(woodwork.pressurePlate().asItem());
        tag.apply(ItemTags.WOODEN_TRAPDOORS).add(woodwork.trapdoor().asItem());
        tag.apply(Tags.Items.FENCE_GATES_WOODEN).add(woodwork.fenceGate().asItem());
    }

    /**
     * call this method in {@link LootTableProvider#getTables()} or other equivalent method.
     *
     * @param consumer consumer to collect table builder
     * @return block loot for add tree
     */
    @SuppressWarnings("JavadocReference")
    public WoodworkBlockLoot addLoots(Consumer<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> consumer) {
        WoodworkBlockLoot loot = new WoodworkBlockLoot(woodwork);
        consumer.accept(Pair.of(() -> loot, LootContextParamSets.BLOCK));
        return loot;
    }

    /**
     * Call in somewhere can get ExistingFileHelper,
     * like {@link net.minecraftforge.forge.event.lifecycle.GatherDataEvent} event
     * @param helper file helper
     */
    public void verifyTexture(ExistingFileHelper helper) {
        ResourceLocation woodName = new ResourceLocation(woodwork.type.name());
        ResourceLocation tex = new ResourceLocation(woodName.getNamespace(), "textures/entity/signs/" + woodName.getPath() + ".png");
        if (!helper.exists(tex, PackType.CLIENT_RESOURCES)) {
            throw new RuntimeException("Not found sign skin at " + tex);
        }
    }
}
