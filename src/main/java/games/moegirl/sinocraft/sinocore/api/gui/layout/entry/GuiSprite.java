package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class GuiSprite {
    private static final Map<String, MapCodec<? extends GuiSprite>> CODECS = new HashMap<>();

    
//    public static final Codec<GuiSprite> CODEC = Codec.STRING.dispatch(GuiSprite::getType, CODECS::get);

    final ResourceLocation path;
}
