package games.moegirl.sinocraft.sinocore.api.block;

import games.moegirl.sinocraft.sinocore.api.tree.Tree;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.material.Material;

/**
 * A class for leaves with tree
 */
public class BlockTreeLeaves extends LeavesBlock implements ITreeBlock {

    private final Tree tree;

    public BlockTreeLeaves(Tree tree, Properties properties) {
        super(properties);
        this.tree = tree;
    }

    public BlockTreeLeaves(Tree tree) {
        this(tree, Properties.of(Material.LEAVES).strength(0.2F)
                .randomTicks()
                .sound(tree.properties().sound())
                .noOcclusion()
                .isValidSpawn((_1, _2, _3, entity) -> entity == EntityType.OCELOT || entity == EntityType.PARROT)
                .isSuffocating((_1, _2, _3) -> false)
                .isViewBlocking((_1, _2, _3) -> false));
    }

    @Override
    public Tree getTree() {
        return tree;
    }
}
