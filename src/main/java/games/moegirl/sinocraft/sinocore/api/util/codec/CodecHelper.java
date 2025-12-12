package games.moegirl.sinocraft.sinocore.api.util.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CodecHelper {
    public static <T> Codec<T> withAlternative(final Codec<T> primary, final Codec<T> secondary) {
        return new AlternativeCodec<>(primary, secondary);
    }

    public static <T> Codec<T> withAlternatives(final Codec<T> primary, final List<Codec<T>> alternatives) {
        var result = primary;
        for (var codec : alternatives) {
            result = withAlternative(result, codec);
        }
        return result;
    }

    public static <T> MapCodec<T> withAlternative(final MapCodec<T> primary, final MapCodec<T> secondary) {
        return new AlternativeMapCodec<>(primary, secondary);
    }

    public static <T> MapCodec<T> withAlternatives(final MapCodec<T> primary, final List<MapCodec<T>> alternatives) {
        var result = primary;
        for (var codec : alternatives) {
            result = withAlternative(result, codec);
        }
        return result;
    }

    public static <T> Codec<T> withDecodingFallback(final Codec<T> primary, final Codec<T> secondary) {
        return Codec.withAlternative(primary, secondary);
    }

    public static <T> Codec<T> withDecodingFallbacks(final Codec<T> primary, final List<Codec<T>> alternatives) {
        var result = primary;
        for (Codec<T> codec : alternatives) {
            result = withDecodingFallback(result, codec);
        }
        return result;
    }

    public static <T> MapCodec<T> withDecodingFallback(final MapCodec<T> primary, final MapCodec<T> secondary) {
        return Codec.mapEither(primary, secondary)
                .xmap(either -> either.map(Function.identity(), Function.identity()), Either::left);
    }

    public static <T> MapCodec<T> withDecodingFallbacks(final MapCodec<T> primary, final List<MapCodec<T>> alternatives) {
        var result = primary;
        for (var codec : alternatives) {
            result = withDecodingFallback(result, codec);
        }
        return result;
    }

    public static <T> MapCodec<T> aliasedFieldOf(final Codec<T> codec, final String name, final String... aliases) {
        var mapCodec = codec.fieldOf(name);
        for (int i = 1; i < aliases.length; i++) {
            mapCodec = withDecodingFallback(mapCodec, codec.fieldOf(aliases[i]));
        }
        return mapCodec;
    }

    public static <T> MapCodec<Optional<T>> optionalAliasedFieldOf(final Codec<T> codec, final String name, final String... aliases) {
        var mapCodec = codec.optionalFieldOf(name);
        for (var i = 1; i < aliases.length; i++) {
            mapCodec = withDecodingFallback(mapCodec, codec.optionalFieldOf(aliases[i]));
        }
        return mapCodec;
    }

    public static <T> MapCodec<T> optionalFieldOf(final MapCodec<T> codec, final String name, T defaultValue) {
        return codec.codec().optionalFieldOf(name, defaultValue);
    }

    public static <T> MapCodec<Optional<T>> optionalFieldOf(final MapCodec<T> codec, final String name) {
        return codec.codec().optionalFieldOf(name);
    }

    public static <T> MapCodec<T> unwarpOptional(final MapCodec<Optional<T>> mapCodec) {
        return mapCodec.xmap(m -> m.orElse(null), Optional::ofNullable);
    }

    public static <T> MapCodec<T> unwarpOptional(final MapCodec<Optional<T>> mapCodec, T defaultValue) {
        return mapCodec.xmap(m -> m.orElse(defaultValue), Optional::ofNullable);
    }
}
