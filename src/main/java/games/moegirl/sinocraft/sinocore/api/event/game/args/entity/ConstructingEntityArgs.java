package games.moegirl.sinocraft.sinocore.api.event.game.args.entity;

import lombok.Getter;
import net.minecraft.world.entity.Entity;

public class ConstructingEntityArgs implements IEntityArgs {
    @Getter
    private final Entity entity;

    public ConstructingEntityArgs(Entity entity) {
        this.entity = entity;
    }
}
