package games.moegirl.sinocraft.sinocore.api.event.game.args.entity;

import games.moegirl.sinocraft.sinocore.api.event.ICancellableArgs;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityJoinLevelArgs extends EntityArgs implements ICancellableArgs {
    @Getter
    private boolean cancelled = false;

    @Getter
    private final Level level;

    @Getter
    private final boolean worldGenSpawned;

    public EntityJoinLevelArgs(Entity entity, Level level) {
        this(entity, level, false);
    }

    public EntityJoinLevelArgs(Entity entity, Level level, boolean worldGenSpawned) {
        super(entity);
        this.level = level;
        this.worldGenSpawned = worldGenSpawned;
    }

    @Override
    public void cancel() {
        this.cancelled = true;
    }
}
