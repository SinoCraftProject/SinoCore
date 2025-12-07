package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public final class ButtonEntry extends AbstractWidgetEntry {
    public static final MapCodec<ButtonEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.listOf().fieldOf("position").forGetter(b -> List.of(b.getX(), b.getY())),
                    Codec.INT.listOf().fieldOf("size").forGetter(b -> List.of(b.getWidth(), b.getHeight())),
                    Codec.either(Codec.STRING, Codec.STRING.listOf())
                            .optionalFieldOf("texture", Either.right(List.of()))
                            .forGetter(e -> Either.right(e.getTexture())),
                    Codec.either(Codec.STRING, Codec.STRING.listOf())
                            .optionalFieldOf("texture_hover", Either.right(List.of()))
                            .forGetter(e -> Either.right(e.getTextureHover())),
                    Codec.either(Codec.STRING, Codec.STRING.listOf())
                            .optionalFieldOf("texture_pressed", Either.right(List.of()))
                            .forGetter(e -> Either.right(e.getTexturePressed())),
                    Codec.either(Codec.STRING, Codec.STRING.listOf())
                            .optionalFieldOf("texture_disable", Either.right(List.of()))
                            .forGetter(e -> Either.right(e.getTextureDisable())),
                    Codec.STRING.optionalFieldOf("tooltip", null).forGetter(ButtonEntry::getTooltip))
            .apply(instance, ButtonEntry::new));

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final List<String> texture;
    private final List<String> textureHover;
    private final List<String> texturePressed;
    private final List<String> textureDisable;

    @Nullable
    private final String tooltip;

    ButtonEntry(List<Integer> position, List<Integer> size, Either<String, List<String>> texture,
                Either<String, List<String>> textureHover, Either<String, List<String>> texturePressed,
                Either<String, List<String>> textureDisable, @Nullable String tooltip) {
        this.x = position.get(0);
        this.y = position.get(1);
        this.width = size.get(0);
        this.height = size.get(1);
        this.texture = texture.map(List::of, List::copyOf);
        this.textureHover = textureHover.map(List::of, List::copyOf);
        this.texturePressed = texturePressed.map(List::of, List::copyOf);
        this.textureDisable = textureDisable.map(List::of, List::copyOf);
        this.tooltip = tooltip;
    }
}
