package games.moegirl.sinocraft.sinocore.mixin;

import games.moegirl.sinocraft.sinocore.api.tree.Tree;
import games.moegirl.sinocraft.sinocore.common.util.mixin.IBoat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Boat.class)
public abstract class MixinBoat implements IBoat {

    private static EntityDataAccessor<String> DATA_ID_TREE;

    /**
     * Inject DATA_ID_TREE data to Boat class
     */
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void injectCInit(CallbackInfo ci) {
        DATA_ID_TREE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.STRING);
    }

    /**
     * Initialize DATA_ID_TREE value to empty
     */
    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    protected void injectDefineSynchedData(CallbackInfo ci) {
        ((Boat) (Object) this).getEntityData().define(DATA_ID_TREE, "");
    }

    /**
     * Drop boat item with tree in {@link Boat#getDropItem()}
     */
    @Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
    protected void injectGetDropItem(CallbackInfoReturnable<Item> cir) {
        Tree tree = getTreeType();
        if (tree != null) {
            cir.setReturnValue(tree.getItems().boat.get());
            cir.cancel();
        }
    }

    /**
     * Save tree name to boat data
     */
    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    protected void injectAddAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        Tree tree = getTreeType();
        if (tree != null) {
            pCompound.putString("sinocore:tree", tree.name.toString());
        }
    }

    /**
     * Read tree name from data
     */
    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void injectReadAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if (pCompound.contains("sinocore:tree", Tag.TAG_STRING)) {
            String name = pCompound.getString("sinocore:tree");
            Tree tree = Tree.get(new ResourceLocation(name));
            if (tree != null) {
                setTreeType(tree);
            }
        }
    }

    /**
     * Modify dropped plank and stick from tree
     */
    @ModifyArg(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    protected ItemLike modifyCheckFallDamage(ItemLike item) {
        Tree tree = getTreeType();
        if (tree != null) {
            if (item instanceof Block) {
                return tree.getBlocks().planks.get();
            } else {
                RegistryObject<Item> stack = tree.getItems().stick;
                return stack == null ? item : stack.get();
            }
        }
        return item;
    }

    @Override
    public Tree getTreeType() {
        String value = ((Boat) (Object) this).getEntityData().get(DATA_ID_TREE);
        if (!value.isEmpty()) {
            return Tree.get(new ResourceLocation(value));
        }
        return null;
    }

    @Override
    public void setTreeType(Tree tree) {
        ((Boat) (Object) this).getEntityData().set(DATA_ID_TREE, tree.name.toString());
    }
}
