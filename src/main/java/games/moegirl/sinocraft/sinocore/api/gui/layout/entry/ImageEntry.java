package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.GuiImage;
import lombok.Getter;

@Getter
public class ImageEntry extends AbstractComponentEntry {
    public static final Codec<ImageEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            GuiImage.MAP_CODEC.forGetter(ImageEntry::getSprite)
    ).apply(instance, ImageEntry::new));

    public static final MapCodec<ImageEntry> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    protected final GuiImage sprite;

    ImageEntry(AbstractComponentEntry entry, GuiImage sprite) {
        super(entry);
        this.sprite = sprite;
    }
}
