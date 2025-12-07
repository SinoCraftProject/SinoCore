package games.moegirl.sinocraft.sinocore.api.event.client;

import games.moegirl.sinocraft.sinocore.api.event.EventFactory;
import games.moegirl.sinocraft.sinocore.api.event.ICancellableEventHandler;
import games.moegirl.sinocraft.sinocore.api.event.IEvent;
import games.moegirl.sinocraft.sinocore.api.event.client.args.render.RenderItemInFrameArgs;

public class RenderEvents {
    public static final IEvent<RenderItemInFrameArgs, ICancellableEventHandler<RenderItemInFrameArgs>> RENDER_ITEM_IN_FRAME = EventFactory.createCancellableEvent(RenderItemInFrameArgs.class);

}
