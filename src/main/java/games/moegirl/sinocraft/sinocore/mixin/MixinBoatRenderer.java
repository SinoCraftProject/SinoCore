package games.moegirl.sinocraft.sinocore.mixin;

import com.mojang.datafixers.util.Pair;
import games.moegirl.sinocraft.sinocore.api.tree.Tree;
import games.moegirl.sinocraft.sinocore.utility.mixin.IBoat;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(BoatRenderer.class)
public abstract class MixinBoatRenderer {

    private final Map<ResourceLocation, Pair<ResourceLocation, BoatModel>> MODEL_CACHES_FROM_SINO = new HashMap<>();

    private EntityRendererProvider.Context context;

    /**
     * Inject to constructor, cached EntityRendererProvider.Context value
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    protected void injectConstructor(EntityRendererProvider.Context p_173936_, CallbackInfo ci) {
        context = p_173936_;
    }

    /**
     * Inject to {@link BoatRenderer#getModelWithLocation(Boat)}, return custom boat layer by tree if exist
     */
    @Inject(method = "getModelWithLocation", at = @At("HEAD"), cancellable = true, remap = false)
    protected void injectGetModelWithLocation(Boat boat, CallbackInfoReturnable<Pair<ResourceLocation, BoatModel>> cir) {
        Tree tree = ((IBoat) boat).getTreeType();
        if (tree != null) {
            cir.setReturnValue(MODEL_CACHES_FROM_SINO.computeIfAbsent(tree.name, name -> {
                ResourceLocation entity = new ResourceLocation(tree.name.getNamespace(),
                        "textures/entity/boat/" + tree.name.getPath() + ".png");
                return Pair.of(entity, new BoatModel(context.bakeLayer(tree.getBoatLayer())));
            }));
            cir.cancel();
        }
    }
}
