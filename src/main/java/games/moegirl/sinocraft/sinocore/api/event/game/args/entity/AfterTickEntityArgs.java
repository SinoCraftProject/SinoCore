package games.moegirl.sinocraft.sinocore.api.event.game.args.entity;

import net.minecraft.world.entity.Entity;

public class AfterTickEntityArgs extends EntityArgs {
    public AfterTickEntityArgs(Entity entity) {
        super(entity);
    }
}
