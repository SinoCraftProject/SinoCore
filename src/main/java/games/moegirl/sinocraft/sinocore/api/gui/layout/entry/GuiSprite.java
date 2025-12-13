package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

@Getter
@AllArgsConstructor
public class GuiSprite {
    public static final Codec<GuiSprite> CODEC = ResourceLocation.CODEC.comapFlatMap(r -> DataResult.success(new GuiSprite(r)), GuiSprite::getPath);
    public static final MapCodec<GuiSprite> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    protected final ResourceLocation path;
}
