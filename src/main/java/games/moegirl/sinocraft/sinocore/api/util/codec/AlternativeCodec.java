package games.moegirl.sinocraft.sinocore.api.util.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import lombok.AllArgsConstructor;

/**
 * Try primary first, if failed try secondary, for both encode and decode.
 * <p>
 * The vanilla one {@link Codec#withAlternative(Codec, Codec)} returns
 * {@link com.mojang.serialization.codecs.EitherCodec} which is actually {@code Codec<Either<T, U>>}. <br/>
 * When encoding, either T or U will be encoded, but not try T then U.
 *
 * @param <TYPE> the type
 */
@AllArgsConstructor
public class AlternativeCodec<TYPE> implements Codec<TYPE> {
    private Codec<TYPE> primary;
    private Codec<TYPE> secondary;

    @Override
    public <FORMAT> DataResult<Pair<TYPE, FORMAT>> decode(DynamicOps<FORMAT> ops, FORMAT input) {
        var result = primary.decode(ops, input);
        if (result.isSuccess()) {
            return result;
        }
        return secondary.decode(ops, input);
    }

    @Override
    public <FORMAT> DataResult<FORMAT> encode(TYPE input, DynamicOps<FORMAT> ops, FORMAT prefix) {
        var result = primary.encode(input, ops, prefix);
        if (result.isSuccess()) {
            return result;
        }
        return secondary.encode(input, ops, prefix);
    }

    @Override
    public String toString() {
        return "AlternativeCodec[" + primary + " | " + secondary + "]";
    }
}
