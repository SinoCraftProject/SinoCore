package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;

import java.util.List;

@Getter
public final class ProgressEntry extends AbstractWidgetEntry {
    public static final MapCodec<ProgressEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.listOf().fieldOf("position").forGetter(b -> List.of(b.getX(), b.getY())),
                    Codec.INT.listOf().fieldOf("size").forGetter(b -> List.of(b.getWidth(), b.getHeight())),
                    Codec.STRING.fieldOf("texture").forGetter(ProgressEntry::getTexture),
                    Codec.STRING.fieldOf("texture_filled").forGetter(ProgressEntry::getTextureFilled),
                    Codec.STRING.optionalFieldOf("direction", "horizontal").forGetter(ProgressEntry::getDirection))
            .apply(instance, ProgressEntry::new));

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String texture;
    private final String textureFilled;
    private final boolean isVertical;
    private final boolean isOpposite;

    ProgressEntry(List<Integer> pos, List<Integer> size, String texture, String textureFilled, String direction) {
        this.x = pos.get(0);
        this.y = pos.get(1);
        this.width = size.get(0);
        this.height = size.get(1);
        this.texture = texture;
        this.textureFilled = textureFilled;

        if (direction != null) {
            isVertical = direction.endsWith("vertical");
            isOpposite = direction.startsWith("-");
        } else {
            isVertical = isOpposite = false;
        }
    }

    private String getDirection() {
        return isVertical ? isOpposite ? "-vertical" : "vertical" : isOpposite ? "-horizontal" : "horizontal";
    }
}
