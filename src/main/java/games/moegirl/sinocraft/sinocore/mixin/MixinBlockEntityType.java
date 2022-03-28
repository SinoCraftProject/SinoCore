package games.moegirl.sinocraft.sinocore.mixin;

import com.google.common.collect.ImmutableSet;
import games.moegirl.sinocraft.sinocore.common.util.mixin.IBlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(BlockEntityType.class)
public abstract class MixinBlockEntityType implements IBlockEntityType {

    @Shadow
    @Final
    @Mutable
    private Set<Block> validBlocks;

    /**
     * Add a method to append block to existed valid block
     *
     * @param block block to append to this type
     */
    @Override
    public void addBlockToEntity(Block block) {
        ImmutableSet.Builder<Block> blocks = ImmutableSet.builder();
        blocks.addAll(validBlocks);
        blocks.add(block);
        validBlocks = blocks.build();
    }
}
