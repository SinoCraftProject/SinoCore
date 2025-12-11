package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record Size(int width, int height) {
    private static final Codec<Size> CODEC_WH = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("width").forGetter(Size::width),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("height").forGetter(Size::height)
    ).apply(instance, Size::new));

    private static final Codec<Size> CODEC_LIST = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.listOf().fieldOf("size").forGetter(p -> List.of(p.width(), p.height()))
    ).apply(instance, l -> new Size(l.get(0), l.get(1))));

    public static final Codec<Size> CODEC = CodecHelper.withAlternative(CODEC_WH, CODEC_LIST);

    public static final Size ZERO = new Size(0, 0);
}
