package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import net.minecraft.util.ExtraCodecs;

public record UV(Position position, Size size) {
    public static final Codec<UV> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecHelper.aliasedFieldOf(ExtraCodecs.NON_NEGATIVE_INT, "uStart", "uPosition", "uOffset").forGetter(UV::getUStart),
            CodecHelper.aliasedFieldOf(ExtraCodecs.NON_NEGATIVE_INT, "vStart", "vPosition", "vOffset").forGetter(UV::getVStart),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("uWidth").forGetter(UV::getUWidth),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("vHeight").forGetter(UV::getVHeight)
    ).apply(instance, UV::new));

    public UV(int uPosition, int vPosition, int uWidth, int vHeight) {
        this(new Position(uPosition, vPosition), new Size(uWidth, vHeight));
    }

    public int getUStart() {
        return position.x();
    }

    public int getVStart() {
        return position.y();
    }

    public int getUEnd() {
        return getUStart() + getUWidth();
    }

    public int getVEnd() {
        return getVStart() + getVHeight();
    }

    public int getUWidth() {
        return size.width();
    }

    public int getVHeight() {
        return size.height();
    }
}
