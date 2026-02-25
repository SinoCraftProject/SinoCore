package games.moegirl.sinocraft.sinocore.api.event.game.args.entity.living.player;

import games.moegirl.sinocraft.sinocore.api.event.CancellableArgsBase;
import games.moegirl.sinocraft.sinocore.api.event.game.args.interact.IInteractArgs;
import lombok.Getter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class PlayerInteractArgs extends CancellableArgsBase implements IPlayerArgs, IInteractArgs {
    @Getter
    private final Player entity;

    @Getter
    private final InteractionHand hand;

    public PlayerInteractArgs(Player player, InteractionHand hand) {
        this.entity = player;
        this.hand = hand;
    }
}
