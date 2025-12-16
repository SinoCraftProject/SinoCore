package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.Direction2D;
import games.moegirl.sinocraft.sinocore.api.gui.GuiImage;
import lombok.Getter;

@Getter
public class ProgressBarEntry extends AbstractComponentEntry {
    public static final MapCodec<ProgressBarEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            GuiImage.CODEC.fieldOf("texture").forGetter(ProgressBarEntry::getTexture),
            GuiImage.CODEC.fieldOf("texture_filled").forGetter(ProgressBarEntry::getTextureFilled),
            Direction2D.CODEC.optionalFieldOf("direction", Direction2D.LEFT_TO_RIGHT).forGetter(ProgressBarEntry::getDirection)
    ).apply(instance, ProgressBarEntry::new));

    protected final GuiImage texture;
    protected final GuiImage textureFilled;
    protected final Direction2D direction;

    ProgressBarEntry(AbstractComponentEntry entry, GuiImage texture, GuiImage textureFilled, Direction2D direction) {
        super(entry);
        this.texture = texture;
        this.textureFilled = textureFilled;
        this.direction = direction;
    }
}
