package games.moegirl.sinocraft.sinocore.api.gui.layout.widget;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.Direction2D;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.GuiSprite;
import lombok.Getter;

@Getter
public final class ProgressEntry extends AbstractWidgetEntry {
    public static final MapCodec<ProgressEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractWidgetEntry.MAP_CODEC.forGetter(e -> e),
            GuiSprite.CODEC.fieldOf("texture").forGetter(ProgressEntry::getTexture),
            GuiSprite.CODEC.fieldOf("texture_filled").forGetter(ProgressEntry::getTextureFilled),
            Direction2D.CODEC.optionalFieldOf("direction", Direction2D.LEFT_TO_RIGHT).forGetter(ProgressEntry::getDirection)
    ).apply(instance, ProgressEntry::new));

    private final GuiSprite texture;
    private final GuiSprite textureFilled;
    private final Direction2D direction;

    ProgressEntry(AbstractWidgetEntry entry, GuiSprite texture, GuiSprite textureFilled, Direction2D direction) {
        super(entry);
        this.texture = texture;
        this.textureFilled = textureFilled;
        this.direction = direction;
    }
}
