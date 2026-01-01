package games.moegirl.sinocraft.sinocore.fabric.mixin.event.game.entity;

import games.moegirl.sinocraft.sinocore.api.event.game.EntityEvents;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.EntityLeaveLevelArgs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.multiplayer.ClientLevel$EntityCallbacks")
public abstract class ClientLevel$EntityCallbacksMixin {
    @Shadow(aliases = {"field_27735"}, remap = false)
    private ClientLevel level;

    @Inject(method = "onTrackingEnd(Lnet/minecraft/world/entity/Entity;)V", at = @At("TAIL"))
    private void tail$onTrackingEnd(Entity entity, CallbackInfo ci) {
        EntityEvents.LEAVE_LEVEL.invoke(new EntityLeaveLevelArgs(entity, level));
    }
}
