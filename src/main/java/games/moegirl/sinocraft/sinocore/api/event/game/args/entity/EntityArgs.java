package games.moegirl.sinocraft.sinocore.api.event.game.args.entity;

import lombok.Getter;
import net.minecraft.world.entity.Entity;

public class EntityArgs {
    @Getter
    private final Entity entity;

    public EntityArgs(Entity entity) {
        this.entity = entity;
    }
}
