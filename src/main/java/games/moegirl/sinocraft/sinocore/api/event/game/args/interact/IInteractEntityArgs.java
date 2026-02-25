package games.moegirl.sinocraft.sinocore.api.event.game.args.interact;

import net.minecraft.world.entity.Entity;

public interface IInteractEntityArgs extends IInteractArgs {
    Entity getTargetEntity();
}
