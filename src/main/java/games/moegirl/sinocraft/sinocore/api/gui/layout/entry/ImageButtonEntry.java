package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.gui.GuiTexture;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Getter
public class ImageButtonEntry extends AbstractComponentEntry {
    public static final MapCodec<ImageButtonEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbstractComponentEntry.MAP_CODEC.forGetter(e -> e),
            GuiTexture.CODEC.fieldOf("texture").forGetter(e -> e.texture),
            CodecHelper.unwarpOptional(
                    GuiTexture.CODEC.optionalFieldOf("texture_hover")
            ).forGetter(e -> e.textureHover),
            CodecHelper.unwarpOptional(
                    GuiTexture.CODEC.optionalFieldOf("texture_pressed")
            ).forGetter(e -> e.texturePressed),
            CodecHelper.unwarpOptional(
                    GuiTexture.CODEC.optionalFieldOf("texture_disabled")
            ).forGetter(e -> e.textureDisabled)
    ).apply(instance, ImageButtonEntry::new));

    protected GuiTexture texture;

    @Nullable
    protected GuiTexture textureHover;

    @Nullable
    protected GuiTexture texturePressed;

    @Nullable
    protected GuiTexture textureDisabled;

    protected ImageButtonEntry(AbstractComponentEntry entry, GuiTexture texture, @Nullable GuiTexture textureHover,
                               @Nullable GuiTexture texturePressed, @Nullable GuiTexture textureDisabled) {
        super(entry);
        this.texture = texture;
        this.textureHover = textureHover;
        this.texturePressed = texturePressed;
        this.textureDisabled = textureDisabled;
    }
}
