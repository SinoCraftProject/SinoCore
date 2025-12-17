package games.moegirl.sinocraft.sinocore.mixin.item;

import games.moegirl.sinocraft.sinocore.api.registry.TabDisplayItemsGenerator;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow
    public abstract Item asItem();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(Item.Properties properties, CallbackInfo ci) {
        for (var e : properties.sino$getTabs()) {
            var key = e.getKey();
            var value = e.getValue();
            var reg = TabDisplayItemsGenerator.getGenerator(key);
            var item = value.apply(asItem());
            reg.addStack(item);
        }
    }
}
