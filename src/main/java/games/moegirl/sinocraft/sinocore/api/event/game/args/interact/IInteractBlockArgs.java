package games.moegirl.sinocraft.sinocore.api.event.game.args.interact;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IInteractBlockArgs extends IInteractArgs {
    BlockPos getTargetPos();

    BlockState getTargetBlockState();
}
