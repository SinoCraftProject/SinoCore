package games.moegirl.sinocraft.sinocore.api.event.game.args.entity;

import games.moegirl.sinocraft.sinocore.api.event.CancellableArgsBase;
import games.moegirl.sinocraft.sinocore.api.event.game.args.level.ILevelArgs;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityJoinLevelArgs extends CancellableArgsBase implements IEntityArgs, ILevelArgs {
    @Getter
    private final Entity entity;

    @Getter
    private final Level level;

    @Getter
    private final boolean worldGenSpawned;

    public EntityJoinLevelArgs(Entity entity, Level level) {
        this(entity, level, false);
    }

    public EntityJoinLevelArgs(Entity entity, Level level, boolean worldGenSpawned) {
        this.entity = entity;
        this.level = level;
        this.worldGenSpawned = worldGenSpawned;
    }
}
