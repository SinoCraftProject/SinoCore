package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Getter
public class ImageButtonEntry extends ButtonEntry {
    public static final MapCodec<ImageButtonEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ButtonEntry.MAP_CODEC.forGetter(e -> e),
            GuiSprite.CODEC.fieldOf("texture").forGetter(e -> e.texture),
            CodecHelper.unwarpOptional(
                    GuiSprite.CODEC.optionalFieldOf("texture_hover")
            ).forGetter(e -> e.textureHover),
            CodecHelper.unwarpOptional(
                    GuiSprite.CODEC.optionalFieldOf("texture_pressed")
            ).forGetter(e -> e.texturePressed),
            CodecHelper.unwarpOptional(
                    GuiSprite.CODEC.optionalFieldOf("texture_disabled")
            ).forGetter(e -> e.textureDisabled)
    ).apply(instance, ImageButtonEntry::new));

    protected GuiSprite texture;

    @Nullable
    protected GuiSprite textureHover;

    @Nullable
    protected GuiSprite texturePressed;

    @Nullable
    protected GuiSprite textureDisabled;

    protected ImageButtonEntry(ButtonEntry buttonEntry, GuiSprite texture, @Nullable GuiSprite textureHover,
                               @Nullable GuiSprite texturePressed, @Nullable GuiSprite textureDisabled) {
        super(buttonEntry);
        this.texture = texture;
        this.textureHover = textureHover;
        this.texturePressed = texturePressed;
        this.textureDisabled = textureDisabled;
    }

    public GuiSprite getTextureHoverOrDefault() {
        return getTextureHover().orElse(texture);
    }

    public GuiSprite getTexturePressedOrDefault() {
        return getTexturePressed().orElse(texture);
    }

    public GuiSprite getTextureDisabledOrDefault() {
        return getTextureDisabled().orElse(texture);
    }

    public Optional<GuiSprite> getTextureHover() {
        return Optional.ofNullable(textureHover);
    }

    public Optional<GuiSprite> getTexturePressed() {
        return Optional.ofNullable(texturePressed);
    }

    public Optional<GuiSprite> getTextureDisabled() {
        return Optional.ofNullable(textureDisabled);
    }
}
