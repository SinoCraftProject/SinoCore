package games.moegirl.sinocraft.sinocore.fabric.mixin.event.game.entity;

import games.moegirl.sinocraft.sinocore.api.event.game.EntityEvents;
import games.moegirl.sinocraft.sinocore.api.event.game.args.entity.EntityJoinLevelArgs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public abstract class PersistentEntitySectionManagerMixin<T extends EntityAccess> {
    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void before$addEntity(T entity, boolean worldGenSpawned, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Entity e) {
            if (EntityEvents.JOIN_LEVEL.invoke(new EntityJoinLevelArgs(e, e.level(), worldGenSpawned)).isCancelled()) {
                cir.setReturnValue(false);
            }
        }
    }
}
