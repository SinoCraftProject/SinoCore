package games.moegirl.sinocraft.sinocore.mixin;

import games.moegirl.sinocraft.sinocore.api.tree.Tree;
import games.moegirl.sinocraft.sinocore.utility.mixin.IBoat;
import games.moegirl.sinocraft.sinocore.utility.mixin.IBoatItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BoatItem.class)
public abstract class MixinBoatItem implements IBoatItem {

    private Tree tree = null;

    @Override
    public BoatItem setTreeType(Tree tree) {
        this.tree = tree;
        return (BoatItem) (Object) this;
    }

    /**
     * Insert after {@link Boat#setType(Boat.Type)}, bind tree to boat if exist
     */
    @Inject(method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;setYRot(F)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    protected void injectUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
                             ItemStack itemstack, HitResult hitresult, Vec3 vec3, double d0, List<Entity> list, Boat boat) {
        if (tree != null) {
            ((IBoat) boat).setTreeType(tree);
        }
    }
}
