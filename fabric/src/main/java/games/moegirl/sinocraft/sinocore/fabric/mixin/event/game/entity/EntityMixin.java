package games.moegirl.sinocraft.sinocore.fabric.mixin.event.game.entity;

import games.moegirl.sinocraft.sinocore.api.event.game.EntityEvents;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.AfterTickEntityArgs;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.BeforeTickEntityArgs;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.ConstructingEntityArgs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void tail$ctor(EntityType<?> entityType, Level level, CallbackInfo ci) {
        EntityEvents.CONSTRUCTING.invoke(new ConstructingEntityArgs((Entity) (Object) this));
    }

    @Redirect(method = "rideTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void invoke$entity$tick(Entity instance) {
        if (!EntityEvents.BEFORE_TICK.invoke(new BeforeTickEntityArgs(instance)).isCancelled()) {
            instance.tick();
            EntityEvents.AFTER_TICK.invoke(new AfterTickEntityArgs(instance));
        }
    }
}