package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.datafixers.util.Either;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImageButtonEntry extends ButtonEntry {
    protected String texture;

    @Nullable
    protected String textureHover;

    @Nullable
    protected String texturePressed;

    @Nullable
    protected String textureDisable;

    ImageButtonEntry(List<Integer> position, List<Integer> size, Either<String, List<String>> texture, Either<String, List<String>> textureHover, Either<String, List<String>> texturePressed, Either<String, List<String>> textureDisable, @Nullable String tooltip) {
        super(position, size, texture, textureHover, texturePressed, textureDisable, tooltip);
    }
}
