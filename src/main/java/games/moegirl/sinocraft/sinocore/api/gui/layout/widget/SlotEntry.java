package games.moegirl.sinocraft.sinocore.api.gui.layout.widget;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.Position;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.GuiSprite;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

/**
 * A slot widget, size indicates the slot background size. <br/>
 * Size of slot is always 18 * 18 in mc (16px item + 2px border). But the background could be larger.
 */
@Getter
public final class SlotEntry extends AbstractWidgetEntry {
    public static final GuiSprite DEFAULT_SLOT_TEXTURE = new GuiSprite(ResourceLocation.withDefaultNamespace("container/slot"));

    public static final MapCodec<SlotEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractWidgetEntry.MAP_CODEC.forGetter(e -> e),
            MapCodec.assumeMapUnsafe(Position.CODEC).fieldOf("slot_offset").forGetter(SlotEntry::getOffset),
            CodecHelper.unwarpOptional(GuiSprite.CODEC.optionalFieldOf("texture"), DEFAULT_SLOT_TEXTURE).forGetter(SlotEntry::getTexture)
    ).apply(instance, SlotEntry::new));

    protected Position offset;
    protected GuiSprite texture;

    protected SlotEntry(AbstractWidgetEntry entry, Position offset, GuiSprite texture) {
        super(entry);
        this.offset = offset;
        this.texture = texture;
    }
}
