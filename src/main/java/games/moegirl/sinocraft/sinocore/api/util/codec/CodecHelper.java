package games.moegirl.sinocraft.sinocore.api.util.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.util.function.Function;

public class CodecHelper {
    public static <T> Codec<T> withAlternative(final Codec<T> primary, final Codec<T> secondary) {
        return new AlternativeCodec<>(primary, secondary);
    }

    public static <T> MapCodec<T> withAlternative(final MapCodec<T> primary, final MapCodec<T> secondary) {
        return new AlternativeMapCodec<>(primary, secondary);
    }

    public static <T> MapCodec<T> mapWithAlternative(final MapCodec<T> primary, final MapCodec<T> secondary) {
        return Codec.mapEither(primary, secondary)
                .xmap(either -> either.map(Function.identity(), Function.identity()), Either::left);
    }

    public static <T> MapCodec<T> aliasedFieldOf(final Codec<T> codec, final String name, final String... aliases) {
        MapCodec<T> mapCodec = codec.fieldOf(name);
        for (int i = 1; i < aliases.length; i++) {
            mapCodec = mapWithAlternative(mapCodec, codec.fieldOf(aliases[i]));
        }
        return mapCodec;
    }
}
