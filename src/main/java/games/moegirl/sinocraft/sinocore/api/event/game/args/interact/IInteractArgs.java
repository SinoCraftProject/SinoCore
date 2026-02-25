package games.moegirl.sinocraft.sinocore.api.event.game.args.interact;

import games.moegirl.sinocraft.sinocore.api.event.ICancellableArgs;
import net.minecraft.world.InteractionHand;

public interface IInteractArgs extends ICancellableArgs {
    InteractionHand getHand();
}
