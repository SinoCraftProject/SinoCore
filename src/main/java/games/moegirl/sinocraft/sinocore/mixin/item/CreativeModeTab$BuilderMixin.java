package games.moegirl.sinocraft.sinocore.mixin.item;

import games.moegirl.sinocraft.sinocore.api.injectable.ISinoCreativeModeTab_Builder;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CreativeModeTab.Builder.class)
public abstract class CreativeModeTab$BuilderMixin implements ISinoCreativeModeTab_Builder {
}
