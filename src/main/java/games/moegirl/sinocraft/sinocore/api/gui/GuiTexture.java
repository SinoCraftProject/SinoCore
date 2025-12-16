package games.moegirl.sinocraft.sinocore.api.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

@Getter
@AllArgsConstructor
public class GuiTexture {
    public static final Codec<GuiTexture> CODEC = ResourceLocation.CODEC.comapFlatMap(r -> DataResult.success(new GuiTexture(r)), GuiTexture::getPath);
    public static final MapCodec<GuiTexture> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    protected final ResourceLocation path;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof GuiTexture other) {
            return this.path.equals(other.path);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return "GuiTexture[" +
                "path=" + path +
                ']';
    }
}
