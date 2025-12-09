package games.moegirl.sinocraft.sinocore.neoforge.event.client.render;

import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.event.client.RenderEvents;
import games.moegirl.sinocraft.sinocore.api.event.client.args.render.RenderItemInFrameArgs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderItemInFrameEvent;

@EventBusSubscriber(modid = SinoCore.MODID, value = Dist.CLIENT)
public class RenderEventImpl {
    @SubscribeEvent
    public static void onRenderInFrame(RenderItemInFrameEvent event) {
        var result = RenderEvents.RENDER_ITEM_IN_FRAME.invoke(new RenderItemInFrameArgs(event.getItemStack(),
                event.getItemFrameEntity(), event.getRenderer(),
                event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight()));
        if (result.isCancelled()) {
            event.setCanceled(true);
        }
    }
}
