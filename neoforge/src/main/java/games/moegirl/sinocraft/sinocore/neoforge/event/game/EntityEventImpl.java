package games.moegirl.sinocraft.sinocore.neoforge.event.game;

import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.event.game.EntityEvents;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = SinoCore.MODID)
public class EntityEventImpl {
    @SubscribeEvent
    public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
        EntityEvents.CONSTRUCTING.invoke(new ConstructingEntityArgs(event.getEntity()));
    }

    @SubscribeEvent
    public static void onEntityTickPre(EntityTickEvent.Pre event) {
        var result = EntityEvents.BEFORE_TICK.invoke(new BeforeTickEntityArgs(event.getEntity()));
        if (result.isCancelled()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityTickPost(EntityTickEvent.Post event) {
        EntityEvents.AFTER_TICK.invoke(new AfterTickEntityArgs(event.getEntity()));
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        var result = EntityEvents.JOIN_LEVEL.invoke(
                new EntityJoinLevelArgs(event.getEntity(), event.getLevel(), event.loadedFromDisk()));
        if (result.isCancelled()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        EntityEvents.LEAVE_LEVEL.invoke(new EntityLeaveLevelArgs(event.getEntity(), event.getLevel()));
    }
}
