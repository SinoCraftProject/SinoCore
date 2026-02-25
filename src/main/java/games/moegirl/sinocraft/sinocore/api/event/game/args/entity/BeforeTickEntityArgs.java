package games.moegirl.sinocraft.sinocore.api.event.game.args.entity;

import games.moegirl.sinocraft.sinocore.api.event.CancellableArgsBase;
import games.moegirl.sinocraft.sinocore.api.event.game.args.lifecycle.ITickArgs;
import lombok.Getter;
import net.minecraft.world.entity.Entity;

public class BeforeTickEntityArgs extends CancellableArgsBase implements IEntityArgs, ITickArgs {
    @Getter
    private final Entity entity;

    public BeforeTickEntityArgs(Entity entity) {
        this.entity = entity;
    }
}
