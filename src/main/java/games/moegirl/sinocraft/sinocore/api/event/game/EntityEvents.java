package games.moegirl.sinocraft.sinocore.api.event.game;

import games.moegirl.sinocraft.sinocore.api.event.EventFactory;
import games.moegirl.sinocraft.sinocore.api.event.ICancellableEventHandler;
import games.moegirl.sinocraft.sinocore.api.event.IEvent;
import games.moegirl.sinocraft.sinocore.api.event.IEventHandler;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.*;

public class EntityEvents {
    public static final IEvent<ConstructingEntityArgs, IEventHandler<ConstructingEntityArgs>> CONSTRUCTING = EventFactory.createEvent(ConstructingEntityArgs.class);

    public static final IEvent<BeforeTickEntityArgs, ICancellableEventHandler<BeforeTickEntityArgs>> BEFORE_TICK = EventFactory.createCancellableEvent(BeforeTickEntityArgs.class);
    public static final IEvent<AfterTickEntityArgs, IEventHandler<AfterTickEntityArgs>> AFTER_TICK = EventFactory.createEvent(AfterTickEntityArgs.class);

    public static final IEvent<EntityJoinLevelArgs, ICancellableEventHandler<EntityJoinLevelArgs>> JOIN_LEVEL = EventFactory.createCancellableEvent(EntityJoinLevelArgs.class);
    public static final IEvent<EntityLeaveLevelArgs, IEventHandler<EntityLeaveLevelArgs>> LEAVE_LEVEL = EventFactory.createEvent(EntityLeaveLevelArgs.class);
}
