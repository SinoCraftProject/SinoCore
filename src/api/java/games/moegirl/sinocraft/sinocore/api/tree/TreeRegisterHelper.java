package games.moegirl.sinocraft.sinocore.api.tree;

import com.mojang.datafixers.util.Pair;
import games.moegirl.sinocraft.sinocore.api.SinoCoreAPI;
import games.moegirl.sinocraft.sinocore.api.data.LanguageProviderBase;
import games.moegirl.sinocraft.sinocore.api.mixin.IBlockEntityTypes;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * * RegisterXxx methods means you must call in the mod, but addXxx is optional.
 */
@SuppressWarnings("JavadocReference")
public record TreeRegisterHelper(Tree tree) {

    /**
     * Register all blocks to register, call before {@link RegistryEvent} event
     *
     * @param register block register
     */
    public void registerBlocks(DeferredRegister<Block> register) {
        tree.blocks = new TreeBlocks(tree, register);
    }

    /**
     * Register all item to register, call before {@link RegistryEvent} event
     *
     * @param register item register
     * @param tab      item creative mod tab
     */
    public void registerItems(DeferredRegister<Item> register, CreativeModeTab tab) {
        tree.items = new TreeItems(tree, register, tab);
    }

    /**
     * modifies sign block entity, call after {@link RegistryEvent} event
     */
    public void registerTileEntityModifiers() {
        IBlockEntityTypes sign = SinoCoreAPI.getMixins().getBlockEntityType(BlockEntityType.SIGN);
        if (!tree.properties.hasCustomSignEntity) {
            sign.addBlockToEntity(tree.getBlocks().sign());
        }
        if (!tree.properties.hasCustomWallSignEntity) {
            sign.addBlockToEntity(tree.getBlocks().wallSign());
        }
        if (tree.properties.hasChest && !tree.properties.hasCustomChestEntity) {
            IBlockEntityTypes chest = SinoCoreAPI.getMixins().getBlockEntityType(BlockEntityType.CHEST);
            chest.addBlockToEntity(tree.getBlocks().chest());
        }
    }

    /**
     * Register block family, use for add recipes. Called after block register event,
     * before {@link RenderTooltipEvent.GatherComponents}
     *
     * <p>You can invoke this in {@link FMLCommonSetupEvent}</p>
     */
    public void registerBlockFamily() {
        for (Method method : BlockFamilies.class.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers())
                    && method.getReturnType() == BlockFamily.Builder.class
                    && method.getParameterCount() == 1
                    && method.getParameterTypes()[0] == Block.class) {
                method.setAccessible(true);
                try {
                    TreeBlocks blocks = tree.getBlocks();
                    BlockFamily.Builder builder = (BlockFamily.Builder) method.invoke(null, blocks.planks());
                    tree.blockFamily = builder.button(blocks.button())
                            .fence(blocks.fence())
                            .fenceGate(blocks.fenceGate())
                            .pressurePlate(blocks.pressurePlate())
                            .sign(blocks.sign(), blocks.wallSign())
                            .slab(blocks.slab())
                            .stairs(blocks.stairs())
                            .door(blocks.door())
                            .trapdoor(blocks.trapdoor())
                            .recipeGroupPrefix("wooden")
                            .recipeUnlockedBy("has_planks")
                            .getFamily();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    /**
     * Register all block's render types, call in {@link FMLClientSetupEvent}
     */
    @OnlyIn(Dist.CLIENT)
    public void registerRenderType() {
        net.minecraft.client.renderer.RenderType cutoutMipped = net.minecraft.client.renderer.RenderType.cutoutMipped();
        net.minecraft.client.renderer.RenderType cutout = net.minecraft.client.renderer.RenderType.cutout();

        setRenderType(tree.getBlocks().leaves(), cutoutMipped);
        setRenderType(tree.getBlocks().sapling(), cutout);
        setRenderType(tree.getBlocks().trapdoor(), cutout);
        setRenderType(tree.getBlocks().pottedSapling(), cutout);
        setRenderType(tree.getBlocks().door(), cutout);
    }

    @OnlyIn(Dist.CLIENT)
    private <T extends Block> void setRenderType(T block, net.minecraft.client.renderer.RenderType type) {
        net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(block, type);
    }

    /**
     * Register boat's layer, call in {@link EntityRenderersEvent.RegisterLayerDefinitions}
     */
    @OnlyIn(Dist.CLIENT)
    public void registerBoatLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ResourceLocation name = tree.getName();
        ResourceLocation location = new ResourceLocation(name.getNamespace(), "boat/" + name.getPath());
        tree.boatLayer = new net.minecraft.client.model.geom.ModelLayerLocation(location, "main");
        event.registerLayerDefinition(tree.getBoatLayer(), net.minecraft.client.model.BoatModel::createBodyModel);
    }

    /**
     * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, add chinese name
     *
     * @param provider language provider
     * @param chinese  tree chinese name
     */
    public void addLanguagesZh(LanguageProvider provider, String chinese) {
        TreeItems items = tree.getItems();
        TreeBlocks blocks = tree.getBlocks();
        provider.addBlock(blocks.planks, chinese + "木板");
        provider.addBlock(blocks.sapling, chinese + "树苗");
        provider.addBlock(blocks.log, chinese + "原木");
        provider.addBlock(blocks.strippedLog, "去皮" + chinese + "原木");
        provider.addBlock(blocks.wood, chinese + "木");
        provider.addBlock(blocks.strippedWoods, "去皮" + chinese + "木");
        provider.addBlock(blocks.leaves, chinese + "树叶");
        provider.addBlock(blocks.sign, chinese + "木告示牌");
        provider.add(Util.makeDescriptionId("block", blocks.wallSign.getId()), "墙上的" + chinese + "木告示牌");
        provider.addBlock(blocks.pressurePlate, chinese + "木压力板");
        provider.addBlock(blocks.trapdoor, chinese + "木活板门");
        provider.addBlock(blocks.stairs, chinese + "木楼梯");
        provider.addBlock(blocks.pottedSapling, chinese + "树苗盆栽");
        provider.addBlock(blocks.button, chinese + "木按钮");
        provider.addBlock(blocks.slab, chinese + "木台阶");
        provider.addBlock(blocks.fenceGate, chinese + "木栅栏门");
        provider.addBlock(blocks.fence, chinese + "木栅栏");
        provider.addBlock(blocks.door, chinese + "木门");
        provider.addItem(items.boat, chinese + "木船");
        if (items.stick != null) {
            provider.addItem(Objects.requireNonNull(items.stick), chinese + "木棍");
        }
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
        TreeItems items = tree.getItems();
        TreeBlocks blocks = tree.getBlocks();
        provider.addBlock(blocks.planks, english + " Planks");
        provider.addBlock(blocks.sapling, english + " Sapling");
        provider.addBlock(blocks.log, english + " Log");
        provider.addBlock(blocks.strippedLog, "Stripped" + english + " Stripped");
        provider.addBlock(blocks.wood, english + " Wood");
        provider.addBlock(blocks.strippedWoods, "Stripped " + english + " Wood");
        provider.addBlock(blocks.leaves, english + " Leaves");
        provider.addBlock(blocks.sign, english + " Sign");
        provider.add(Util.makeDescriptionId("block", blocks.wallSign.getId()), english + " Wall Sign");
        provider.addBlock(blocks.pressurePlate, english + " Pressure Plate");
        provider.addBlock(blocks.trapdoor, english + " Trap Trapdoor");
        provider.addBlock(blocks.stairs, english + " Stairs");
        provider.addBlock(blocks.pottedSapling, "Potted " + english + " Sapling");
        provider.addBlock(blocks.button, english + " Button");
        provider.addBlock(blocks.slab, english + " Slab");
        provider.addBlock(blocks.fenceGate, english + " Fence Gate");
        provider.addBlock(blocks.fence, english + " Fence");
        provider.addBlock(blocks.door, english + " Door");
        provider.addItem(items.boat, english + " Boat");
        if (items.stick != null) {
            provider.addItem(Objects.requireNonNull(items.stick), english + " Stick");
        }
    }

    /**
     * call this method in {@link LanguageProviderBase#addTranslations()} or other equivalent method,
     * add english and chinese languages in one method
     *
     * @param addBlock method to add block language, str1 is english name, str2 is chinese name
     * @param addItem  method to add item language, str1 is english name, str2 is chinese name
     * @param addName  method to add language by string, str1 is english name, str2 is chinese name
     * @param english  tree english name
     * @param chinese  tree chinese name
     */
    public void addLanguages(LanguageProviderBase provider, String chinese) {
        addLanguages(provider, defaultEnglishName(), chinese);
    }

    /**
     * call this method in {@link LanguageProviderBase#addTranslations()} or other equivalent method,
     * add english and chinese languages in one method
     *
     * @param addBlock method to add block language, str1 is english name, str2 is chinese name
     * @param addItem  method to add item language, str1 is english name, str2 is chinese name
     * @param addName  method to add language by string, str1 is english name, str2 is chinese name
     * @param english  tree english name
     * @param chinese  tree chinese name
     */
    public void addLanguages(LanguageProviderBase provider, String english, String chinese) {
        TreeItems items = tree.getItems();
        TreeBlocks blocks = tree.getBlocks();
        provider.addBlock(blocks.planks, english + " Planks", chinese + "木板");
        provider.addBlock(blocks.sapling, english + " Sapling", chinese + "树苗");
        provider.addBlock(blocks.log, english + " Log", chinese + "原木");
        provider.addBlock(blocks.strippedLog, "Stripped" + english + " Stripped", "去皮" + chinese + "原木");
        provider.addBlock(blocks.wood, english + " Wood", chinese + "木");
        provider.addBlock(blocks.strippedWoods, "Stripped " + english + " Wood", "去皮" + chinese + "木");
        provider.addBlock(blocks.leaves, english + " Leaves", chinese + "树叶");
        provider.addBlock(blocks.sign, english + " Sign", chinese + "木告示牌");
        provider.add(Util.makeDescriptionId("block", blocks.wallSign.getId()),
                english + " Wall Sign", "墙上的" + chinese + "木告示牌");
        provider.addBlock(blocks.pressurePlate, english + " Pressure Plate", chinese + "木压力板");
        provider.addBlock(blocks.trapdoor, english + " Trap Trapdoor", chinese + "木活板门");
        provider.addBlock(blocks.stairs, english + " Stairs", chinese + "木楼梯");
        provider.addBlock(blocks.pottedSapling, "Potted " + english + " Sapling", chinese + "树苗盆栽");
        provider.addBlock(blocks.button, english + " Button", chinese + "木按钮");
        provider.addBlock(blocks.slab, english + " Slab", chinese + "木台阶");
        provider.addBlock(blocks.fenceGate, english + " Fence Gate", chinese + "木栅栏门");
        provider.addBlock(blocks.fence, english + " Fence", chinese + "木栅栏");
        provider.addBlock(blocks.door, english + " Door", chinese + "木门");
        provider.addItem(items.boat, english + " Boat", chinese + "木船");
        if (items.stick != null) {
            provider.addItem(items.stick, english + " Stick", chinese + "木棍");
        }
    }

    private String defaultEnglishName() {
        StringBuilder sb = new StringBuilder();
        for (String s : tree.getName().getPath().split("_")) {
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
        ExistingFileHelper helper = provider.models().existingFileHelper;
        ExistingFileHelper.ResourceType texType =
                new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

        TreeBlocks blocks = tree.getBlocks();
        ResourceLocation texPlank = provider.blockTexture(blocks.planks());

        provider.simpleBlock(blocks.planks());
        provider.simpleBlock(blocks.sapling(),
                provider.models().cross(blocks.sapling.getId().getPath(), provider.blockTexture(blocks.sapling())));
        provider.logBlock(blocks.log());
        provider.logBlock(blocks.strippedLog());
        provider.simpleBlock(blocks.wood(), provider.models().cubeColumn(blocks.wood.getId().getPath(),
                provider.blockTexture(blocks.log()),
                provider.blockTexture(blocks.log())));
        provider.simpleBlock(blocks.strippedWoods(), provider.models().cubeColumn(blocks.strippedWoods.getId().getPath(),
                provider.blockTexture(blocks.strippedLog()),
                provider.blockTexture(blocks.strippedLog())));
        provider.simpleBlock(blocks.leaves(), provider.models().singleTexture(
                blocks.leaves.getId().getPath(),
                provider.mcLoc(ModelProvider.BLOCK_FOLDER + "/leaves"),
                "all", provider.blockTexture(blocks.leaves())));
        provider.signBlock(blocks.sign(), blocks.wallSign(), texPlank);
        provider.pressurePlateBlock(blocks.pressurePlate(), texPlank);
        // orientable?
        provider.trapdoorBlock(blocks.trapdoor(), texPlank, true);
        provider.stairsBlock(blocks.stairs(), texPlank);
        provider.simpleBlock(blocks.pottedSapling(), provider.models().singleTexture(
                blocks.pottedSapling.getId().getPath(),
                provider.mcLoc(ModelProvider.BLOCK_FOLDER + "/flower_pot_cross"),
                "plant", provider.modLoc(ModelProvider.BLOCK_FOLDER + "/" + blocks.sapling.getId().getPath())));
        provider.buttonBlock(blocks.button(), texPlank);
        provider.slabBlock(blocks.slab(), texPlank, texPlank);
        provider.fenceGateBlock(blocks.fenceGate(), texPlank);
        provider.fenceBlock(blocks.fence(), texPlank);

        DoorBlock door = blocks.door();
        ResourceLocation doorName = blocks.door.getId();
        ResourceLocation doorTop = new ResourceLocation(doorName.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + doorName.getPath() + "_top");
        ResourceLocation doorBottom = new ResourceLocation(doorName.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + doorName.getPath() + "_bottom");
        if (!helper.exists(doorTop, texType)) {
            System.out.println(doorTop + " not exist, use " + texPlank);
            doorTop = texPlank;
        }
        if (!helper.exists(doorBottom, texType)) {
            System.out.println(doorBottom + " not exist, use " + texPlank);
            doorBottom = texPlank;
        }
        provider.doorBlock(door, doorBottom, doorTop);

        provider.models().fenceInventory(blocks.fence.getId().getPath() + "_inventory", texPlank);

        SinoCoreAPI.LOGGER.warn("Chest block model not generated, please create it yourself");
    }

    /**
     * call this method in {@link ItemModelProvider#registerModels} or other equivalent method.
     *
     * @param provider item model provider
     */
    public void addItemModels(ItemModelProvider provider) {
        TreeItems items = tree.getItems();
        TreeBlocks blocks = tree.getBlocks();

        addBlockItem(blocks.planks, provider);
        addBlockItem(blocks.sapling, provider);
        addBlockItem(blocks.log, provider);
        addBlockItem(blocks.strippedLog, provider);
        addBlockItem(blocks.strippedWoods, provider);
        addBlockItem(blocks.wood, provider);
        addBlockItem(blocks.leaves, provider);
        addBlockItem(blocks.slab, provider);
        addBlockItem(blocks.fence, "inventory", provider);
        addBlockItem(blocks.stairs, provider);
        addBlockItem(blocks.button, provider);
        addBlockItem(blocks.pressurePlate, provider);
        addBlockItem(blocks.trapdoor, "bottom", provider);
        addBlockItem(blocks.fenceGate, provider);

        addItem(items.boat, "generated", provider);
        addItem(blocks.door, "generated", provider);
        addItem(blocks.sign, "generated", provider);
        if (items.stick != null) {
            addItem(Objects.requireNonNull(items.stick), "handheld", provider);
        }

        SinoCoreAPI.LOGGER.warn("Chest item model not generated, please create it yourself");
    }

    private void addBlockItem(RegistryObject<? extends Block> block, ItemModelProvider provider) {
        String path = block.getId().getPath();
        provider.withExistingParent(path, provider.modLoc("block/" + path));
    }

    private void addBlockItem(RegistryObject<? extends Block> block, String postfix, ItemModelProvider provider) {
        String path = block.getId().getPath();
        provider.withExistingParent(path, provider.modLoc("block/" + path + "_" + postfix));
    }

    private void addItem(RegistryObject<? extends ItemLike> item, String type, ItemModelProvider provider) {
        ResourceLocation name = item.get().asItem().delegate.name();
        provider.singleTexture(name.getPath(), provider.mcLoc("item/" + type),
                "layer0", provider.modLoc("item/" + name.getPath()));
    }

    /**
     * call this method in {@link RecipeProvider#buildCraftingRecipes}
     * or other equivalent method.
     *
     * @param consumer save consumer
     */
    @SuppressWarnings("JavadocReference")
    public void addRecipes(Consumer<FinishedRecipe> consumer) {
        TreeItems items = tree.getItems();
        TreeBlocks blocks = tree.getBlocks();
        registerBlockFamily();

        InventoryChangeTrigger.TriggerInstance hasLogs = InventoryChangeTrigger.TriggerInstance
                .hasItems(ItemPredicate.Builder.item().of(ItemTags.LOGS).build());
        InventoryChangeTrigger.TriggerInstance hasLog = InventoryChangeTrigger.TriggerInstance
                .hasItems(ItemPredicate.Builder.item().of(blocks.log()).build());
        InventoryChangeTrigger.TriggerInstance hasPlank = InventoryChangeTrigger.TriggerInstance
                .hasItems(ItemPredicate.Builder.item().of(blocks.planks()).build());
        EnterBlockTrigger.TriggerInstance inWater = EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.WATER);
        // planksFromLog
        ShapelessRecipeBuilder.shapeless(blocks.planks(), 4).group("planks")
                .requires(blocks.log())
                .unlockedBy("has_log", hasLogs)
                .save(consumer);
        // woodFromLogs
        ShapedRecipeBuilder.shaped(blocks.wood(), 3).group("bark")
                .define('#', blocks.log())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_log", hasLog)
                .save(consumer);
        ShapedRecipeBuilder.shaped(blocks.strippedWoods(), 3).group("bark")
                .define('#', blocks.strippedLog())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_log", hasLog)
                .save(consumer);
        // woodenBoat
        ShapedRecipeBuilder.shaped(items.boat()).group("boat")
                .define('#', blocks.wood())
                .pattern("# #")
                .pattern("###")
                .unlockedBy("in_water", inWater)
                .save(consumer);
        // chest
        if (blocks.hasChest()) {
            ShapedRecipeBuilder.shaped(blocks.chest()).group("chest")
                    .define('#', blocks.planks())
                    .pattern("###")
                    .pattern("# #")
                    .pattern("###")
                    .unlockedBy("has_planks", hasPlank)
                    .save(consumer);
        }

        if (tree.getProperties().hasStick()) {
            ShapedRecipeBuilder.shaped(Objects.requireNonNull(items.stick()), 4).group("sticks")
                    .define('#', blocks.wood())
                    .pattern("#")
                    .pattern("#")
                    .unlockedBy("has_planks", hasPlank)
                    .save(consumer);
        }
    }

    /**
     * call this method in {@link TagsProvider#addTags} or other equivalent method.
     *
     * @param tag tag appender creator, use {@link TagsProvider#tag}
     */
    public void addBlockTags(Function<TagKey<Block>, TagsProvider.TagAppender<Block>> tag) {
        String name = (tree.getName().getPath() + "_logs").toLowerCase(Locale.ROOT);
        tree.tagLogs = BlockTags.create(new ResourceLocation(tree.getName().getNamespace(), name));
        TreeBlocks blocks = tree.getBlocks();
        tag.apply(tree.tagLogs).add(blocks.log(), blocks.strippedLog(), blocks.wood(), blocks.strippedLog());

        tag.apply(BlockTags.PLANKS).add(blocks.planks());
        tag.apply(BlockTags.WOODEN_BUTTONS).add(blocks.button());
        tag.apply(BlockTags.WOODEN_DOORS).add(blocks.door());
        tag.apply(BlockTags.WOODEN_STAIRS).add(blocks.stairs());
        tag.apply(BlockTags.WOODEN_SLABS).add(blocks.slab());
        tag.apply(BlockTags.WOODEN_FENCES).add(blocks.fence());
        tag.apply(BlockTags.SAPLINGS).add(blocks.sapling());
        tag.apply(BlockTags.LOGS_THAT_BURN).addTag(tree.tagLogs);
        tag.apply(BlockTags.FLOWER_POTS).add(blocks.pottedSapling());
        tag.apply(BlockTags.WOODEN_PRESSURE_PLATES).add(blocks.pressurePlate());
        tag.apply(BlockTags.LEAVES).add(blocks.leaves());
        tag.apply(BlockTags.WOODEN_TRAPDOORS).add(blocks.trapdoor());
        tag.apply(BlockTags.STANDING_SIGNS).add(blocks.sign());
        tag.apply(BlockTags.WALL_SIGNS).add(blocks.wallSign());
        tag.apply(BlockTags.FENCE_GATES).add(blocks.fenceGate());
        if (blocks.hasChest()) {
            tag.apply(Tags.Blocks.CHESTS_WOODEN).add(blocks.chest());
        }

        tag.apply(Tags.Blocks.FENCE_GATES_WOODEN).add(blocks.fenceGate());
    }

    /**
     * call this method in {@link TagsProvider#addTags} or other equivalent method.
     *
     * @param tag tag appender creator, use {@link TagsProvider#tag}
     */
    public void addItemTags(Function<TagKey<Item>, TagsProvider.TagAppender<Item>> tag) {
        TreeItems items = tree.getItems();
        tag.apply(ItemTags.BOATS).add(items.boat());
        if (items.stick != null) {
            tag.apply(Tags.Items.RODS_WOODEN).add(Objects.requireNonNull(items.stick()));
        }
    }

    /**
     * call this method in {@link LootTableProvider#getTables()} or other equivalent method.
     *
     * @param consumer consumer to collect table builder
     * @return block loot for add tree
     */
    public TreeBlockLoot addLoots(Consumer<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> consumer) {
        TreeBlockLoot loot = new TreeBlockLoot(tree.getBlocks());
        consumer.accept(Pair.of(() -> loot, LootContextParamSets.BLOCK));
        return loot;
    }
}
