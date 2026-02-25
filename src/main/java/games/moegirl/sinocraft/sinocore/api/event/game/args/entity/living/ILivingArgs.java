package games.moegirl.sinocraft.sinocore.api.event.game.args.entity.living;

import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.IEntityArgs;
import net.minecraft.world.entity.LivingEntity;

public interface ILivingArgs extends IEntityArgs {
    @Override
    LivingEntity getEntity();
}
