package games.moegirl.sinocraft.sinocore.api.impl.mixin;

import games.moegirl.sinocraft.sinocore.api.mixin.IBlockEntityTypes;
import games.moegirl.sinocraft.sinocore.utility.mixin.IBlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record MBlockEntityTypes(BlockEntityType<?> type) implements IBlockEntityTypes {

    @Override
    public void addBlockToEntity(Block block) {
        ((IBlockEntityType) type).addBlockToEntity(block);
    }
}
