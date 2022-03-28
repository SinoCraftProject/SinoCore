package games.moegirl.sinocraft.sinocore.api.tree;

import games.moegirl.sinocraft.sinocore.api.block.*;
import games.moegirl.sinocraft.sinocore.api.world.TreeFeatureBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class TreeBuilder {

    final ResourceLocation name;
    boolean hasStick = false;
    boolean hasChest = false;
    boolean customSignEntity = false;
    boolean customWallSignEntity = false;
    boolean customChestEntity = false;
    SoundType sound = SoundType.GRASS;
    MaterialColor topLogColor = MaterialColor.WOOD;
    MaterialColor barkLogColor = MaterialColor.WOOD;
    MaterialColor topStrippedLogColor = MaterialColor.WOOD;
    MaterialColor barkStrippedLogColor = MaterialColor.WOOD;
    MaterialColor plankColor = MaterialColor.WOOD;
    MaterialColor woodColor = MaterialColor.WOOD;
    MaterialColor strippedWoodColor = MaterialColor.WOOD;
    AbstractTreeGrower grower = new OakTreeGrower();

    Function<Tree, Block> planks = BlockTreePlanks::new;
    Function<Tree, SaplingBlock> sapling = BlockTreeSapling::new;
    Function<Tree, RotatedPillarBlock> log = tree -> new BlockTreeLog(tree, false);
    Function<Tree, RotatedPillarBlock> strippedLog = tree -> new BlockTreeLog(tree, true);
    Function<Tree, RotatedPillarBlock> wood = tree -> new BlockTreeWood(tree, false);
    Function<Tree, RotatedPillarBlock> strippedWoods = tree -> new BlockTreeWood(tree, true);
    Function<Tree, LeavesBlock> leaves = BlockTreeLeaves::new;
    Function<Tree, StandingSignBlock> sign = tree ->
            new StandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD, tree.getBlocks().log().defaultMaterialColor())
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.WOOD), tree.type);
    Function<Tree, WallSignBlock> wallSign = tree ->
            new WallSignBlock(BlockBehaviour.Properties.of(Material.WOOD, tree.blocks.log().defaultMaterialColor())
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .lootFrom(tree.blocks.sign), tree.type);
    Function<Tree, PressurePlateBlock> pressurePlate = tree -> {
        assert tree.blocks != null;
        return new PressurePlateBlock(
                PressurePlateBlock.Sensitivity.EVERYTHING,
                BlockBehaviour.Properties.of(Material.WOOD, tree.blocks.planks().defaultMaterialColor())
                        .noCollission()
                        .strength(0.5F)
                        .sound(SoundType.WOOD));
    };
    Function<Tree, TrapDoorBlock> trapdoor = tree ->
            new TrapDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, tree.properties.woodColor)
                    .strength(3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .isValidSpawn((_1, _2, _3, _4) -> false));
    Function<Tree, StairBlock> stairs = tree -> new StairBlock(() -> tree.blocks.planks().defaultBlockState(),
            BlockBehaviour.Properties.copy(tree.blocks.planks()));
    Function<Tree, FlowerPotBlock> pottedSapling = tree -> new FlowerPotBlock(
            () -> (FlowerPotBlock) Blocks.FLOWER_POT.delegate.get(), tree.blocks.sapling,
            BlockBehaviour.Properties.of(Material.DECORATION)
                    .instabreak()
                    .noOcclusion());
    Function<Tree, ButtonBlock> button = tree -> new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION)
            .noCollission()
            .strength(0.5F)
            .sound(SoundType.WOOD));
    Function<Tree, SlabBlock> slab = tree -> new SlabBlock(BlockBehaviour.Properties.of(Material.WOOD, tree.properties.woodColor)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    Function<Tree, FenceGateBlock> fenceGate = tree ->
            new FenceGateBlock(BlockBehaviour.Properties.of(Material.WOOD, tree.blocks.planks().defaultMaterialColor())
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD));
    Function<Tree, FenceBlock> fence = tree ->
            new FenceBlock(BlockBehaviour.Properties.of(Material.WOOD, tree.blocks.planks().defaultMaterialColor())
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD));
    Function<Tree, DoorBlock> door = tree ->
            new DoorBlock(BlockBehaviour.Properties.of(Material.WOOD, tree.blocks.planks().defaultMaterialColor())
                    .strength(3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion());
    Function<Tree, ChestBlock> chest = tree ->
            new ChestBlock(BlockBehaviour.Properties.copy(Blocks.CHEST).color(plankColor), () -> BlockEntityType.CHEST);

    public TreeBuilder(ResourceLocation name) {
        this.name = name;
    }

    /**
     * Leaves: set leaves sound, all vanilla leaves is {@link SoundType#GRASS},
     * except azalea is {@link  SoundType#AZALEA_LEAVES}
     *
     * @param sound leave sound, default is {@link SoundType#GRASS}
     * @return this builder
     */
    public TreeBuilder leavesSound(SoundType sound) {
        this.sound = sound;
        return this;
    }

    /**
     * Log: set log color in map
     *
     * @param top  top color, default is {@link MaterialColor#WOOD}
     * @param bark bark color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder logColor(MaterialColor top, MaterialColor bark) {
        this.topLogColor = top;
        this.barkLogColor = bark;
        return this;
    }

    /**
     * Log: set stripped log color in map
     *
     * @param top  top color, default is {@link MaterialColor#WOOD}
     * @param bark bark color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder strippedLogColor(MaterialColor top, MaterialColor bark) {
        this.topStrippedLogColor = top;
        this.barkStrippedLogColor = bark;
        return this;
    }

    /**
     * Plank: set plank color in map
     *
     * @param color plank color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder plankColor(MaterialColor color) {
        this.plankColor = color;
        return this;
    }

    /**
     * Wood: set wood color in map
     *
     * @param color wood color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder woodColor(MaterialColor color) {
        this.woodColor = color;
        return this;
    }

    /**
     * Plank: set stripped wood color in map
     *
     * @param color wood color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder strippedWoodColor(MaterialColor color) {
        this.strippedWoodColor = color;
        return this;
    }

    /**
     * Plank: set wood color in map
     *
     * @param normal   wood color, default is {@link MaterialColor#WOOD}
     * @param stripped stripped plank color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder woodColor(MaterialColor normal, MaterialColor stripped) {
        woodColor(normal);
        strippedWoodColor(stripped);
        return this;
    }

    /**
     * Set all log color in map
     *
     * @param topColor      top log color
     * @param woodColor     wood color
     * @param barkColor     bark log color
     * @param strippedColor stripped log color
     * @param plankColor    plank color
     * @return this builder
     */
    public TreeBuilder color(MaterialColor topColor, MaterialColor woodColor, MaterialColor barkColor, MaterialColor strippedColor, MaterialColor plankColor) {
        logColor(topColor, barkColor);
        strippedLogColor(topColor, strippedColor);
        woodColor(woodColor, strippedColor);
        plankColor(plankColor);
        return this;
    }

    /**
     * Set all log color in map
     *
     * @param woodColor     top log color, plank color and wood color
     * @param barkColor     bark log color
     * @param strippedColor stripped log color
     * @return this builder
     */
    public TreeBuilder color(MaterialColor woodColor, MaterialColor barkColor, MaterialColor strippedColor) {
        return color(woodColor, woodColor, barkColor, strippedColor, woodColor);
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param grower tree grower
     * @return this builder
     */
    public TreeBuilder grower(AbstractTreeGrower grower) {
        this.grower = grower;
        return this;
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param feature feature selector
     * @return this builder
     */
    public TreeBuilder grower(Function<Random, ConfiguredFeature<?, ?>> feature) {
        this.grower = new AbstractTreeGrower() {
            private ConfiguredFeature<?, ?> configuredFeature = null;
            private boolean initialized = false;

            @Override
            protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random pRandom, boolean pLargeHive) {
                if (!initialized) {
                    configuredFeature = feature.apply(pRandom);
                    initialized = true;
                }
                return Holder.direct(configuredFeature);
            }
        };
        return this;
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param feature feature getter
     * @return this builder
     */
    public TreeBuilder grower(Supplier<ConfiguredFeature<?, ?>> feature) {
        return grower(r -> feature.get());
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param feature feature builder
     * @return this builder
     */
    public TreeBuilder growerBuilder(TreeFeatureBuilder feature) {
        return growerBuilder(r -> feature);
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param feature feature builder selector
     * @return this builder
     */
    public TreeBuilder growerBuilder(Function<Random, TreeFeatureBuilder> feature) {
        return grower(r -> feature.apply(r).configured(this.name.getNamespace(), this.name.getPath()));
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param feature feature
     * @return this builder
     */
    public TreeBuilder grower(ConfiguredFeature<?, ?> feature) {
        return grower(r -> feature);
    }

    /**
     * Has special stick item
     */
    public TreeBuilder hasStick() {
        hasStick = true;
        return this;
    }

    /**
     * Has special chest
     */
    public TreeBuilder hasChest() {
        hasChest = true;
        return this;
    }

    public TreeBuilder customPlanks(Function<Tree, Block> factory) {
        this.planks = factory;
        return this;
    }

    public TreeBuilder customSapling(Function<Tree, SaplingBlock> factory) {
        this.sapling = factory;
        return this;
    }

    public TreeBuilder customLog(Function<Tree, RotatedPillarBlock> factory) {
        this.log = factory;
        return this;
    }

    public TreeBuilder customStrippedLog(Function<Tree, RotatedPillarBlock> factory) {
        this.strippedLog = factory;
        return this;
    }

    public TreeBuilder customWood(Function<Tree, RotatedPillarBlock> factory) {
        this.wood = factory;
        return this;
    }

    public TreeBuilder customStrippedWood(Function<Tree, RotatedPillarBlock> factory) {
        this.strippedWoods = factory;
        return this;
    }

    public TreeBuilder customLeaves(Function<Tree, LeavesBlock> factory) {
        this.leaves = factory;
        return this;
    }

    public TreeBuilder customSign(Function<Tree, StandingSignBlock> factory) {
        return customSign(true, factory);
    }

    /**
     * Set custom sign block factory
     *
     * @param customEntity true if use custom block entity
     * @param factory      factory to create block
     * @return this builder
     */
    public TreeBuilder customSign(boolean customEntity, Function<Tree, StandingSignBlock> factory) {
        this.sign = factory;
        this.customSignEntity = customEntity;
        return this;
    }

    public TreeBuilder customWallSign(Function<Tree, WallSignBlock> factory) {
        return customWallSign(true, factory);
    }

    /**
     * Set custom sign (on a wall) block factory
     *
     * @param customEntity true if use custom block entity
     * @param factory      factory to create block
     * @return this builder
     */
    public TreeBuilder customWallSign(boolean customEntity, Function<Tree, WallSignBlock> factory) {
        this.wallSign = factory;
        this.customWallSignEntity = customEntity;
        return this;
    }

    public TreeBuilder customPressurePlate(Function<Tree, PressurePlateBlock> factory) {
        this.pressurePlate = factory;
        return this;
    }

    public TreeBuilder customTrapDoor(Function<Tree, TrapDoorBlock> factory) {
        this.trapdoor = factory;
        return this;
    }

    public TreeBuilder customStairs(Function<Tree, StairBlock> factory) {
        this.stairs = factory;
        return this;
    }

    public TreeBuilder customPottedSapling(Function<Tree, FlowerPotBlock> factory) {
        this.pottedSapling = factory;
        return this;
    }

    public TreeBuilder customButton(Function<Tree, ButtonBlock> factory) {
        this.button = factory;
        return this;
    }

    public TreeBuilder customSlab(Function<Tree, SlabBlock> factory) {
        this.slab = factory;
        return this;
    }

    public TreeBuilder customFenceGate(Function<Tree, FenceGateBlock> factory) {
        this.fenceGate = factory;
        return this;
    }

    public TreeBuilder customFence(Function<Tree, FenceBlock> factory) {
        this.fence = factory;
        return this;
    }

    public TreeBuilder customDoor(Function<Tree, DoorBlock> factory) {
        this.door = factory;
        return this;
    }

    public TreeBuilder customChest(Function<Tree, ChestBlock> factory) {
        return customChest(true, factory);
    }

    /**
     * Set custom chest block factory
     *
     * @param customEntity true if use custom block entity
     * @param factory      factory to create block
     * @return this builder
     */
    public TreeBuilder customChest(boolean customEntity, Function<Tree, ChestBlock> factory) {
        this.chest = factory;
        this.customChestEntity = customEntity;
        this.hasChest = true;
        return this;
    }

    public Tree build() {
        return new Tree(this);
    }
}
