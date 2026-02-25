package games.moegirl.sinocraft.sinocore.api.event.game.args.entity;

import games.moegirl.sinocraft.sinocore.api.event.game.args.level.ILevelArgs;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityLeaveLevelArgs implements IEntityArgs, ILevelArgs {
    @Getter
    private final Entity entity;

    @Getter
    private final Level level;

    public EntityLeaveLevelArgs(Entity entity, Level level) {
        this.entity = entity;
        this.level = level;
    }
}
