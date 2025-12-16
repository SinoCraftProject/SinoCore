package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.Direction2D;
import games.moegirl.sinocraft.sinocore.api.gui.GuiTexture;
import lombok.Getter;

@Getter
public class ProgressBarEntry extends AbstractComponentEntry {
    public static final MapCodec<ProgressBarEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            GuiTexture.CODEC.fieldOf("texture").forGetter(ProgressBarEntry::getTexture),
            GuiTexture.CODEC.fieldOf("texture_filled").forGetter(ProgressBarEntry::getTextureFilled),
            Direction2D.CODEC.optionalFieldOf("direction", Direction2D.LEFT_TO_RIGHT).forGetter(ProgressBarEntry::getDirection)
    ).apply(instance, ProgressBarEntry::new));

    protected final GuiTexture texture;
    protected final GuiTexture textureFilled;
    protected final Direction2D direction;

    ProgressBarEntry(AbstractComponentEntry entry, GuiTexture texture, GuiTexture textureFilled, Direction2D direction) {
        super(entry);
        this.texture = texture;
        this.textureFilled = textureFilled;
        this.direction = direction;
    }
}
