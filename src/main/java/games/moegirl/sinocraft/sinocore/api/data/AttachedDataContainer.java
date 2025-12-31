package games.moegirl.sinocraft.sinocore.api.data;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.stream.Stream;

public class AttachedDataContainer implements IDataContainer {
    public static final Codec<AttachedDataContainer> CODEC = new Codec<>() {
        private <T> DataResult<Unit> parseEntry(final DataResult<Unit> result, final DynamicOps<T> ops,
                                                final Pair<T, T> input,
                                                final Map<DataType<?>, Object> entries,
                                                final Stream.Builder<Pair<T, T>> failed) {
            final T key = input.getFirst();
            final T value = input.getSecond();
            final DataResult<ResourceLocation> keyResult = ResourceLocation.CODEC.parse(ops, key);
            final DataResult<Integer> versionResult = ops.get(value, "version").flatMap(v -> Codec.INT.parse(ops, v));
            final DataResult<DataType<Object>> typeResult = keyResult.flatMap(id -> versionResult.map(version -> new DataKey(id, version)))
                    .flatMap(k -> {
                        Optional<DataType<?>> optional = DataTypes.get(k);
                        //noinspection unchecked
                        return optional.map(t -> DataResult.success((DataType<Object>) t))
                                .orElseGet(() -> DataResult.error(() -> "Unknown data type: " + k));
                    });
            final DataResult<Object> dataResult = ops.get(value, "data").flatMap(data -> typeResult.flatMap(t -> {
                if (t.canBeSaved()) {
                    return DataResult.success(t);
                } else {
                    return DataResult.error(() -> "Data type " + t.getKey() + " has no codec");
                }
            }).flatMap(t -> Objects.requireNonNull(t.getCodec()).parse(ops, data)));
            final DataResult<Pair<DataType<?>, Object>> entryResult = typeResult.apply2stable(Pair::of, dataResult);
            final Optional<Pair<DataType<?>, Object>> entry = entryResult.resultOrPartial();
            if (entry.isPresent()) {
                final DataType<?> type = entry.get().getFirst();
                final Object data = entry.get().getSecond();
                if (entries.putIfAbsent(type, data) != null) {
                    failed.add(input);
                    return result.apply2stable((u, p) -> u, DataResult.error(() -> "Duplicate entry for key: '" + key + "'"));
                }
            }

            if (entryResult.isError()) {
                failed.add(input);
            }

            return result.apply2stable((u, p) -> u, entryResult);
        }

        @Override
        public <T> DataResult<Pair<AttachedDataContainer, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).flatMap(map -> {
                final Map<DataType<?>, Object> entries = new Object2ObjectArrayMap<>();
                final Stream.Builder<Pair<T, T>> failed = Stream.builder();

                final DataResult<Unit> finalResult = map.entries().reduce(
                        DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
                        (result, entry) -> parseEntry(result, ops, entry, entries, failed),
                        (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
                );
                final Pair<Map<DataType<?>, Object>, T> pair = Pair.of(ImmutableMap.copyOf(entries), input);
                final T errors = ops.createMap(failed.build());

                var r = finalResult.map(ignored -> pair).setPartial(pair).mapError(error -> error + " missed input: " + errors);
                return r.map(m -> m.mapFirst(AttachedDataContainer::new));
            });
        }

        @SuppressWarnings("unchecked")
        private <T, DATA> DataResult<T> encodeValue(final Codec<DATA> codec, final Object input, final DynamicOps<T> ops) {
            return codec.encodeStart(ops, (DATA) input);
        }

        @Override
        public <T> DataResult<T> encode(AttachedDataContainer input, DynamicOps<T> ops, T prefix) {
            final RecordBuilder<T> mapBuilder = ops.mapBuilder();
            for (final Map.Entry<DataType<?>, Object> entry : input.data.entrySet()) {
                var type = entry.getKey();
                if (!type.canBeSaved()) {
                    continue;
                }
                var key = ResourceLocation.CODEC.encodeStart(ops, type.getKey().id());
                var value = ops.mapBuilder();
                value.add("version", Codec.INT.encodeStart(ops, type.getKey().version()));
                value.add("data", encodeValue(Objects.requireNonNull(type.getCodec()), entry.getValue(), ops));
                mapBuilder.add(key, value.build(prefix));
            }
            return mapBuilder.build(prefix);
        }
    };

    private final Map<DataType<?>, Object> data;

    public AttachedDataContainer() {
        this(new HashMap<>());
    }

    public AttachedDataContainer(Map<DataType<?>, Object> data) {
        this.data = new Object2ObjectArrayMap<>(data);
    }

    @Override
    public <DATA> void set(DataType<DATA> type, DATA data) {
        this.data.put(type, data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <DATA> DATA get(DataType<DATA> type) {
        return (DATA) this.data.get(type);
    }

    @Override
    public <DATA> boolean has(DataType<DATA> type) {
        return this.data.containsKey(type);
    }

    @Override
    public <DATA> void remove(DataType<DATA> type) {
        this.data.remove(type);
    }

    @Override
    public Map<DataType<?>, Object> getAll() {
        return ImmutableMap.copyOf(data);
    }
}
