package games.moegirl.sinocraft.sinocore.neoforge.mixin.datagen;

import net.neoforged.neoforge.common.data.LanguageProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(LanguageProvider.class)
public interface LanguageProviderAccessor {
    @Accessor("data")
    Map<String, String> sinocore$getData();
}
