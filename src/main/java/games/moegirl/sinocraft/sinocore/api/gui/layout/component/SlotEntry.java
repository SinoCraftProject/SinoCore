package games.moegirl.sinocraft.sinocore.api.gui.layout.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.*;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

/**
 * A slot widget, size indicates the slot background size. <br/>
 * Size of slot is always 18 * 18 in mc (16px item + 2px border). But the background could be larger.
 */
@Getter
public class SlotEntry extends AbstractComponentEntry {
    public static final int SLOT_SIZE = 18;

    public static final GuiSprite DEFAULT_SLOT_TEXTURE = new GuiNineSliceSprite(ResourceLocation.withDefaultNamespace("container/slot"), new Size(18, 18), new Border(1, 1, 1, 1));

    public static final Codec<SlotEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            MapCodec.assumeMapUnsafe(Position.CODEC).fieldOf("slot_offset").forGetter(SlotEntry::getOffset),
            CodecHelper.unwarpOptional(GuiSprite.CODEC.optionalFieldOf("texture"), DEFAULT_SLOT_TEXTURE).forGetter(SlotEntry::getTexture)
    ).apply(instance, SlotEntry::new));

    public static final MapCodec<SlotEntry> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    protected Position offset;
    protected GuiSprite texture;

    protected SlotEntry(AbstractComponentEntry entry) {
        this(entry, Position.ZERO, DEFAULT_SLOT_TEXTURE);
    }

    protected SlotEntry(AbstractComponentEntry entry, Position offset) {
        this(entry, offset, DEFAULT_SLOT_TEXTURE);
    }

    protected SlotEntry(AbstractComponentEntry entry, Position offset, GuiSprite texture) {
        super(entry);
        this.offset = offset;
        this.texture = texture;
    }
}
