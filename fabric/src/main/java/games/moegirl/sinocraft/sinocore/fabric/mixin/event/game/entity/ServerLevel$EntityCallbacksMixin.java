package games.moegirl.sinocraft.sinocore.fabric.mixin.event.game.entity;

import games.moegirl.sinocraft.sinocore.api.event.game.EntityEvents;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.EntityLeaveLevelArgs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.level.ServerLevel$EntityCallbacks")
public abstract class ServerLevel$EntityCallbacksMixin {
    @Shadow(aliases = {"field_26936"}, remap = false)
    private ServerLevel level;

    @Inject(method = "onTrackingEnd(Lnet/minecraft/world/entity/Entity;)V", at = @At("TAIL"))
    private void tail$onTrackingEnd(Entity entity, CallbackInfo ci) {
        EntityEvents.LEAVE_LEVEL.invoke(new EntityLeaveLevelArgs(entity, level));
    }
}
