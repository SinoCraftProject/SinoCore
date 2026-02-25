package games.moegirl.sinocraft.sinocore.api.event.game.args.entity.living.player;

import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.living.ILivingArgs;
import net.minecraft.world.entity.player.Player;

public interface IPlayerArgs extends ILivingArgs {
    @Override
    Player getEntity();
}
