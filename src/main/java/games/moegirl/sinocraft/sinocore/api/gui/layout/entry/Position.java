package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record Position(int x, int y) {
    private static final Codec<Position> CODEC_XY = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("x").forGetter(Position::x),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y").forGetter(Position::y)
    ).apply(instance, Position::new));

    private static final Codec<Position> CODEC_LIST = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.listOf().fieldOf("position").forGetter(p -> List.of(p.x(), p.y()))
    ).apply(instance, l -> new Position(l.get(0), l.get(1))));

    public static final Codec<Position> CODEC = Codec.withAlternative(CODEC_XY, CODEC_LIST);
}
