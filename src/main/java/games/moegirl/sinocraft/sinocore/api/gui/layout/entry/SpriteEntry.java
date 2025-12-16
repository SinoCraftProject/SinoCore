package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.GuiTexture;
import lombok.Getter;

@Getter
public class SpriteEntry extends AbstractComponentEntry {
    public static final Codec<SpriteEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            GuiTexture.MAP_CODEC.forGetter(SpriteEntry::getSprite)
    ).apply(instance, SpriteEntry::new));

    public static final MapCodec<SpriteEntry> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    protected final GuiTexture sprite;

    SpriteEntry(AbstractComponentEntry entry, GuiTexture sprite) {
        super(entry);
        this.sprite = sprite;
    }
}
