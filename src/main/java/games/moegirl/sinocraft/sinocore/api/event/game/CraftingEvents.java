package games.moegirl.sinocraft.sinocore.api.event.game;

import games.moegirl.sinocraft.sinocore.api.event.EventFactory;
import games.moegirl.sinocraft.sinocore.api.event.ICancellableEventHandler;
import games.moegirl.sinocraft.sinocore.api.event.IEvent;
import games.moegirl.sinocraft.sinocore.api.event.game.args.crafting.CartographyTableCraftArgs;

public class CraftingEvents {
    public static final IEvent<CartographyTableCraftArgs, ICancellableEventHandler<CartographyTableCraftArgs>> CARTOGRAPHY_CRAFT = EventFactory.createCancellableEvent(CartographyTableCraftArgs.class);

}
