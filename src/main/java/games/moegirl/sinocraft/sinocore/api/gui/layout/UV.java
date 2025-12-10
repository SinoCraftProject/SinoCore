package games.moegirl.sinocraft.sinocore.api.gui.layout;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UV {
    public static final MapCodec<UV> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecHelper.aliasedFieldOf(Codec.INT, "uPosition", "uOffset").forGetter(UV::getUPosition),
            CodecHelper.aliasedFieldOf(Codec.INT, "vPosition", "vOffset").forGetter(UV::getVPosition),
            Codec.INT.fieldOf("uWidth").forGetter(UV::getUWidth),
            Codec.INT.fieldOf("vHeight").forGetter(UV::getVHeight),
            Codec.INT.optionalFieldOf("textureWidth", 256).forGetter(UV::getTextureWidth),
            Codec.INT.optionalFieldOf("textureHeight", 256).forGetter(UV::getTextureHeight)
    ).apply(instance, UV::new));

    Point position;
    Size size;
    Size textureSize;

    public UV(int uPosition, int vPosition, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        this(new Point(uPosition, vPosition), new Size(uWidth, vHeight), new Size(textureWidth, textureHeight));
    }

    public int getUPosition() {
        return position.getX();
    }

    public int getVPosition() {
        return position.getY();
    }

    public int getUWidth() {
        return size.getWidth();
    }

    public int getVHeight() {
        return size.getHeight();
    }

    public int getTextureWidth() {
        return textureSize.getWidth();
    }

    public int getTextureHeight() {
        return textureSize.getHeight();
    }

    public float getU0() {
        return ((float) getUPosition()) / getTextureWidth();
    }

    public float getV0() {
        return ((float) getVPosition()) / getTextureHeight();
    }

    public float getU1() {
        return ((float) (getUPosition() + getUWidth())) / getTextureWidth();
    }

    public float getV1() {
        return ((float) (getVPosition() + getVHeight())) / getTextureHeight();
    }
}
