package games.moegirl.sinocraft.sinocore.api.event.client;

import games.moegirl.sinocraft.sinocore.api.event.EventFactory;
import games.moegirl.sinocraft.sinocore.api.event.IEvent;
import games.moegirl.sinocraft.sinocore.api.event.IEventHandler;
import games.moegirl.sinocraft.sinocore.api.event.client.args.model.AfterBakeArgs;

public class ModelEvents {
    public static final IEvent<AfterBakeArgs, IEventHandler<AfterBakeArgs>> AFTER_BAKE = EventFactory.createEvent(AfterBakeArgs.class);

}
