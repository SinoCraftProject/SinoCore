package games.moegirl.sinocraft.sinocore.api.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

@Getter
@AllArgsConstructor
public class GuiSprite extends GuiImage {
    public static final Codec<GuiSprite> CODEC = ResourceLocation.CODEC.comapFlatMap(r -> DataResult.success(new GuiSprite(r)), GuiSprite::getPath);
    public static final MapCodec<GuiSprite> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

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
        return "GuiSprite[" +
                "path=" + path +
                ']';
    }

    @Override
    public void blit(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        guiGraphics.blitSprite(path, x, y, width, height);
    }
}
