package games.moegirl.sinocraft.sinocore.api.gui.layout;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UV {
    public static final MapCodec<Size> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("width").forGetter(Size::getWidth),
            Codec.INT.fieldOf("height").forGetter(Size::getHeight)
    ).apply(instance, Size::new));

    int u;
    int v;
}
