package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import games.moegirl.sinocraft.sinocore.api.util.codec.CodecHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

@Getter
@AllArgsConstructor
public abstract class GuiSprite {
    private static final Codec<GuiSprite> SIMPLE_RL_CODEC = ResourceLocation.CODEC.comapFlatMap(r -> DataResult.success(new GuiTexture(r)), GuiSprite::getPath);
    private static final Codec<GuiSprite> TYPE_DISPATCH_CODEC = Codec.STRING.dispatch(Type::getType, Type::getCodec);

    public static final Codec<GuiSprite> CODEC = CodecHelper.withDecodingFallback(SIMPLE_RL_CODEC, TYPE_DISPATCH_CODEC);
    public static final MapCodec<GuiSprite> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    final ResourceLocation path;

    @Getter
    public static enum Type {
        TEXTURE("texture", GuiTexture.class, GuiTexture.MAP_CODEC),
        NINE_PATCH("nine_patch", GuiNineSliceSprite.class, GuiNineSliceSprite.MAP_CODEC),
        ;

        private final String name;
        private final Class<? extends GuiSprite> clazz;
        private final MapCodec<? extends GuiSprite> mapCodec;

        <T extends GuiSprite> Type(String name, Class<T> clazz, MapCodec<T> mapCodec) {
            this.name = name;
            this.clazz = clazz;
            this.mapCodec = mapCodec;
        }

        public static String getType(GuiSprite obj) {
            for (var type : values()) {
                if (type.getClazz() == obj.getClass()) {
                    return type.name;
                }
            }
            throw new IllegalArgumentException("No GuiSprite Type for object: " + obj);
        }

        public static MapCodec<? extends GuiSprite> getCodec(String name) {
            for (var type : values()) {
                if (type.getName().equals(name)) {
                    return type.mapCodec;
                }
            }
            throw new IllegalArgumentException("No GuiSprite Type for name: " + name);
        }
    }
}
