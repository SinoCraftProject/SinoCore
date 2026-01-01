package games.moegirl.sinocraft.sinocore.fabric.mixin.event.game.entity;

import games.moegirl.sinocraft.sinocore.api.event.game.EntityEvents;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.AfterTickEntityArgs;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.BeforeTickEntityArgs;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.EntityJoinLevelArgs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @Redirect(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void invoke$entity$tick(Entity instance) {
        if (!EntityEvents.BEFORE_TICK.invoke(new BeforeTickEntityArgs(instance)).isCancelled()) {
            instance.tick();
            EntityEvents.AFTER_TICK.invoke(new AfterTickEntityArgs(instance));
        }
    }

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void before$addEntity(Entity entity, CallbackInfo ci) {
        if (EntityEvents.JOIN_LEVEL.invoke(new EntityJoinLevelArgs(entity, (ClientLevel) (Object) this)).isCancelled()) {
            ci.cancel();
        }
    }
}
