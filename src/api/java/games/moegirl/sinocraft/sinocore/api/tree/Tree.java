package games.moegirl.sinocraft.sinocore.api.tree;

import games.moegirl.sinocraft.sinocore.api.utility.FloatModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A set of elements about a tree, contains items, blocks, tags, recipes ... and more.
 */
public class Tree {

    private static final Map<ResourceLocation, Tree> TREE_BY_NAME = new HashMap<>();

    public static Optional<Tree> get(ResourceLocation name) {
        return Optional.ofNullable(TREE_BY_NAME.get(name));
    }

    public static Optional<Tree> get(String name) {
        return get(new ResourceLocation(name));
    }

    public static TreeBuilder builder(ResourceLocation name) {
        return new TreeBuilder(name);
    }

    public static TreeBuilder builder(String modid, String name) {
        return builder(new ResourceLocation(modid, name));
    }

    public final RegistryObject<SaplingBlock> sapling;
    public final RegistryObject<RotatedPillarBlock> log;
    public final RegistryObject<RotatedPillarBlock> strippedLog;
    public final RegistryObject<RotatedPillarBlock> wood;
    public final RegistryObject<RotatedPillarBlock> strippedWoods;
    public final RegistryObject<LeavesBlock> leaves;
    public final RegistryObject<FlowerPotBlock> pottedSapling;

    private final Set<Block> allBlocks = new HashSet<>();

    private final Set<Item> allItems = new HashSet<>();

    TagKey<Block> tagLogs;
    TagKey<Item> tagItemLogs;
    TreeRegister register = new TreeRegister(this);
    private final BuilderProperties properties;

    Tree(TreeBuilder builder, DeferredRegister<Block> blocks, DeferredRegister<Item> items) {
        properties = new BuilderProperties(
                builder.name,
                builder.tab,
                builder.sound,
                builder.topLogColor,
                builder.topStrippedLogColor,
                builder.barkLogColor,
                builder.barkStrippedLogColor,
                builder.grower,
                builder.woodColor,
                builder.strippedWoodColor,
                builder.strengthModifier);

        if (builder.grower instanceof TreeSaplingGrower tsg) {
            tsg.setTree(this);
        }

        sapling = register(blocks, "sapling", asSupplier(builder.sapling, allBlocks));
        pottedSapling = register(blocks, "potted", "sapling", asSupplier(builder.pottedSapling, allBlocks));
        log = register(blocks, "log", asSupplier(builder.log, allBlocks));
        strippedLog = register(blocks, "stripped", "log", asSupplier(builder.strippedLog, allBlocks));
        wood = register(blocks, "wood", asSupplier(builder.wood, allBlocks));
        strippedWoods = register(blocks, "stripped", "wood", asSupplier(builder.strippedWoods, allBlocks));
        leaves = register(blocks, "leaves", asSupplier(builder.leaves, allBlocks));

        register(items, sapling, asSupplier(builder.saplingItem, allItems));
        register(items, log, asSupplier(builder.logItem, allItems));
        register(items, strippedLog, asSupplier(builder.strippedLogItem, allItems));
        register(items, wood, asSupplier(builder.woodItem, allItems));
        register(items, strippedWoods, asSupplier(builder.strippedWoodsItem, allItems));
        register(items, leaves, asSupplier(builder.leavesItem, allItems));

        tagLogs = BlockTags.create(new ResourceLocation(properties.name.getNamespace(),
                (properties.name.getPath() + "_logs").toLowerCase(Locale.ROOT)));
        tagItemLogs = ItemTags.create(tagLogs().location());

        TREE_BY_NAME.put(properties.name, this);
    }

    private <T> Supplier<T> asSupplier(Function<Tree, T> factory, Set<? super T> collector) {
        return () -> {
            T value = factory.apply(this);
            collector.add(value);
            return value;
        };
    }

    private <T extends IForgeRegistryEntry<T>, V extends T> RegistryObject<V> register(DeferredRegister<T> register,
                                                                                       String prefix, String postfix,
                                                                                       Supplier<V> supplier) {
        return register.register(prefix + "_" + properties.name.getPath() + "_" + postfix, supplier);
    }

    private <T extends IForgeRegistryEntry<T>, V extends T> RegistryObject<V> register(DeferredRegister<T> register,
                                                                                       String postfix, Supplier<V> supplier) {
        return register.register(properties.name.getPath() + "_" + postfix, supplier);
    }

    private void register(DeferredRegister<Item> item, RegistryObject<? extends Block> block, Supplier<? extends Item> supplier) {
        item.register(block.getId().getPath(), supplier);
    }

    public SaplingBlock sapling() {
        return sapling.get();
    }

    public RotatedPillarBlock log() {
        return log.get();
    }

    public RotatedPillarBlock strippedLog() {
        return strippedLog.get();
    }

    public RotatedPillarBlock wood() {
        return wood.get();
    }

    public RotatedPillarBlock strippedWoods() {
        return strippedWoods.get();
    }

    public FlowerPotBlock pottedSapling() {
        return pottedSapling.get();
    }

    public LeavesBlock leaves() {
        return leaves.get();
    }

    public Set<Block> allBlocks() {
        return Set.copyOf(allBlocks);
    }

    public Set<Item> allItems() {
        return Set.copyOf(allItems);
    }

    public ResourceLocation name() {
        return properties.name;
    }

    public TagKey<Block> tagLogs() {
        return Objects.requireNonNull(tagLogs);
    }

    public TagKey<Item> tagItemLogs() {
        return Objects.requireNonNull(tagItemLogs);
    }

    public TreeRegister register() {
        return register;
    }

    public BuilderProperties properties() {
        return properties;
    }

    public record BuilderProperties(ResourceLocation name,
                                    CreativeModeTab tab,
                                    SoundType sound,
                                    MaterialColor topLogColor,
                                    MaterialColor topStrippedLogColor,
                                    MaterialColor barkLogColor,
                                    MaterialColor barkStrippedLogColor,
                                    AbstractTreeGrower grower,
                                    MaterialColor woodColor,
                                    MaterialColor strippedWoodColor,
                                    FloatModifier strengthModifier) {
    }
}
