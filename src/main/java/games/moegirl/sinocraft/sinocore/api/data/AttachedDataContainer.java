package games.moegirl.sinocraft.sinocore.api.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AttachedDataContainer<HOLDER extends ISinoDataHolder> implements IDataContainer<HOLDER> {
    public static final Codec<AttachedDataContainer<ISinoDataHolder>> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<AttachedDataContainer<ISinoDataHolder>, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMapEntries(input).flatMap(map -> {
                final ImmutableList.Builder<Pair<String, V>> read = ImmutableList.builder();
                final ImmutableMap.Builder<T, T> failed = ImmutableMap.builder();

                final AtomicReference<DataResult<Unit>> result = new AtomicReference<>(DataResult.success(Unit.INSTANCE, Lifecycle.experimental()));

                map.accept((key, value) -> {
                    final DataResult<K> k = keyCodec.parse(ops, key);
                    final DataResult<V> v = elementCodec.parse(ops, value);

                    final DataResult<Pair<K, V>> readEntry = k.apply2stable(Pair::new, v);

                    readEntry.error().ifPresent(e -> failed.put(key, value));

                    result.setPlain(result.getPlain().apply2stable((u, e) -> {
                        read.add(e);
                        return u;
                    }, readEntry));
                });

                final ImmutableList<Pair<K, V>> elements = read.build();
                final T errors = ops.createMap(failed.build());

                final Pair<List<Pair<K, V>>, T> pair = Pair.of(elements, errors);

                return result.getPlain().map(unit -> pair).setPartial(pair);
            });
            return null;
        }

        @Override
        public <T> DataResult<T> encode(AttachedDataContainer<ISinoDataHolder> input, DynamicOps<T> ops, T prefix) {
            return null;
        }
    };

    @Override
    public <DATA> void set(DataType<DATA> type, DATA data) {

    }

    @Override
    public <DATA> DATA get(DataType<DATA> type) {
        return null;
    }

    @Override
    public <DATA> boolean has(DataType<DATA> type) {
        return false;
    }

    @Override
    public <DATA> void remove(DataType<DATA> type) {

    }

    @Override
    public Map<DataType<Object>, Object> getAll() {
        return Map.of();
    }
}
