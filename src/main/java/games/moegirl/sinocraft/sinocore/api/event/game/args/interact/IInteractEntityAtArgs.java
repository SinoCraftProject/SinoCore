package games.moegirl.sinocraft.sinocore.api.event.game.args.interact;

import net.minecraft.world.phys.Vec3;

public interface IInteractEntityAtArgs extends IInteractEntityArgs {
    Vec3 getHitVec();
}
