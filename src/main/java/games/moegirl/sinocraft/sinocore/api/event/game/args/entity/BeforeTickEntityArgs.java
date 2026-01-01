package games.moegirl.sinocraft.sinocore.api.event.game.args.entity;

import games.moegirl.sinocraft.sinocore.api.event.ICancellableArgs;
import lombok.Getter;
import net.minecraft.world.entity.Entity;

public class BeforeTickEntityArgs extends EntityArgs implements ICancellableArgs {
    @Getter
    private boolean cancelled = false;

    public BeforeTickEntityArgs(Entity entity) {
        super(entity);
    }

    @Override
    public void cancel() {
        this.cancelled = true;
    }
}
