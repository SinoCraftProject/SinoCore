package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.GuiImage;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class ImageButtonEntry extends AbstractComponentEntry {
    public static final MapCodec<ImageButtonEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            GuiImage.CODEC.fieldOf("texture").forGetter(e -> e.texture),
            CodecHelper.unwarpOptional(
                    GuiImage.CODEC.optionalFieldOf("texture_hover")
            ).forGetter(e -> e.textureHover),
            CodecHelper.unwarpOptional(
                    GuiImage.CODEC.optionalFieldOf("texture_pressed")
            ).forGetter(e -> e.texturePressed),
            CodecHelper.unwarpOptional(
                    GuiImage.CODEC.optionalFieldOf("texture_disabled")
            ).forGetter(e -> e.textureDisabled)
    ).apply(instance, ImageButtonEntry::new));

    protected GuiImage texture;

    @Nullable
    protected GuiImage textureHover;

    @Nullable
    protected GuiImage texturePressed;

    @Nullable
    protected GuiImage textureDisabled;

    protected ImageButtonEntry(AbstractComponentEntry entry, GuiImage texture, @Nullable GuiImage textureHover,
                               @Nullable GuiImage texturePressed, @Nullable GuiImage textureDisabled) {
        super(entry);
        this.texture = texture;
        this.textureHover = textureHover;
        this.texturePressed = texturePressed;
        this.textureDisabled = textureDisabled;
    }
}
