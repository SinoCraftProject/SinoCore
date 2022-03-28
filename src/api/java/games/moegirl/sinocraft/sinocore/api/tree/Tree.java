package games.moegirl.sinocraft.sinocore.api.tree;

import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A set of elements about a tree, contains items, blocks, tags, recipes ... and more.
 */
public class Tree {

    private static final Map<ResourceLocation, Tree> TREE_BY_NAME = new HashMap<>();
    private static final Map<WoodType, Tree> TREE_BY_WOOD_TYPE = new HashMap<>();

    @Nullable
    public static Tree get(ResourceLocation name) {
        return TREE_BY_NAME.get(name);
    }

    @Nullable
    public static Tree get(WoodType name) {
        return TREE_BY_WOOD_TYPE.get(name);
    }

    public static TreeBuilder builder(ResourceLocation name) {
        return new TreeBuilder(name);
    }

    public static TreeBuilder builder(String modid, String name) {
        return builder(new ResourceLocation(modid, name));
    }

    @Nullable
    TreeBlocks blocks;
    @Nullable
    TreeItems items;
    @Nullable
    BlockFamily blockFamily;
    @Nullable
    TagKey<Block> tagLogs;
    @Nullable
    Object boatLayer;
    TreeRegisterHelper register = new TreeRegisterHelper(this);

    public final TreeProperties properties;
    public final ResourceLocation name;
    public final WoodType type;

    Tree(TreeBuilder builder) {
        properties = new TreeProperties(this, builder);
        this.name = properties.name;
        this.type = properties.type;
        TREE_BY_NAME.put(name, this);
        TREE_BY_WOOD_TYPE.put(type, this);
    }

    public ResourceLocation getName() {
        return name;
    }

    public WoodType getType() {
        return type;
    }

    public BlockFamily getBlockFamily() {
        return Objects.requireNonNull(blockFamily);
    }

    public TagKey<Block> getTagLogs() {
        return Objects.requireNonNull(tagLogs);
    }

    public TreeBlocks getBlocks() {
        return Objects.requireNonNull(blocks, "Please register blocks before use");
    }

    public TreeItems getItems() {
        return Objects.requireNonNull(items, "Please register items before use");
    }

    public TreeProperties getProperties() {
        return properties;
    }

    @OnlyIn(Dist.CLIENT)
    public net.minecraft.client.model.geom.ModelLayerLocation getBoatLayer() {
        return Objects.requireNonNull((net.minecraft.client.model.geom.ModelLayerLocation) boatLayer);
    }

    public TreeRegisterHelper getRegister() {
        return register;
    }
}
