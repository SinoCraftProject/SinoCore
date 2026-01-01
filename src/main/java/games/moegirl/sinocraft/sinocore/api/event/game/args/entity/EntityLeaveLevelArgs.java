package games.moegirl.sinocraft.sinocore.api.event.game.args.entity;

import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityLeaveLevelArgs extends EntityArgs {
    @Getter
    private final Level level;

    public EntityLeaveLevelArgs(Entity entity, Level level) {
        super(entity);
        this.level = level;
    }
}
