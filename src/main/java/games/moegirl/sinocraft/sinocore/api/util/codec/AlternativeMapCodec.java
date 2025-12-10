package games.moegirl.sinocraft.sinocore.api.util.codec;

import com.mojang.serialization.*;
import lombok.AllArgsConstructor;

import java.util.stream.Stream;

/**
 * Try primary first, if failed try secondary, for both encode and decode. <br/>
 * Similar to {@link AlternativeCodec} but for {@link MapCodec}.
 *
 * @see AlternativeCodec
 * @param <TYPE> the type
 */
@AllArgsConstructor
public class AlternativeMapCodec<TYPE> extends MapCodec<TYPE> {
    private MapCodec<TYPE> primary;
    private MapCodec<TYPE> secondary;

    @Override
    public <FORMAT> Stream<FORMAT> keys(DynamicOps<FORMAT> ops) {
        return Stream.concat(primary.keys(ops), secondary.keys(ops)).distinct();
    }

    @Override
    public <FORMAT> DataResult<TYPE> decode(DynamicOps<FORMAT> ops, MapLike<FORMAT> input) {
        var result = primary.decode(ops, input);
        if (result.isSuccess()) {
            return result;
        }
        return secondary.decode(ops, input);
    }

    @Override
    public <FORMAT> RecordBuilder<FORMAT> encode(TYPE input, DynamicOps<FORMAT> ops, RecordBuilder<FORMAT> prefix) {
        var result = primary.encode(input, ops, prefix).build(ops.empty());
        if (result.isSuccess()) {
            return primary.encode(input, ops, prefix);
        }
        return secondary.encode(input, ops, prefix);
    }

    @Override
    public String toString() {
        return "AlternativeMapCodec[" + primary + " | " + secondary + "]";
    }
}
