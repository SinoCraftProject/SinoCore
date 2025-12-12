package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

@Getter
public class GuiNineSliceSprite extends GuiSprite {
    public static final Codec<GuiNineSliceSprite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("path").forGetter(GuiNineSliceSprite::getPath),
            Size.CODEC.fieldOf("size").forGetter(GuiNineSliceSprite::getSize),
            Border.CODEC.fieldOf("border").forGetter(GuiNineSliceSprite::getBorder)
    ).apply(instance, GuiNineSliceSprite::new));

    public static final MapCodec<GuiNineSliceSprite> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    final Size size;
    final Border border;

    public GuiNineSliceSprite(ResourceLocation path, Size size, Border border) {
        super(path);
        this.size = size;
        this.border = border;
    }
}
