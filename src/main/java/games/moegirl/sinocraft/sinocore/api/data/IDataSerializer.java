package games.moegirl.sinocraft.sinocore.api.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

/**
 * Serialize data object from/to structured container (e.g. {@link CompoundTag}, {@link JsonElement}).
 * <p>
 * It is used for data storage.
 *
 * @param <DATA> Data type.
 * @param <CONTAINER> Structured data container, usually {@link CompoundTag}.
 */
public interface IDataSerializer<DATA, CONTAINER> {
    CONTAINER serialize(HolderLookup.Provider registries, DATA data);

    DATA deserialize(HolderLookup.Provider registries, CONTAINER container);

    static <DATA, CONTAINER> IDataSerializer<DATA, CONTAINER> fromCodec(Codec<DATA> codec, DynamicOps<CONTAINER> ops) {
        return new IDataSerializer<DATA, CONTAINER>() {
            @Override
            public CONTAINER serialize(HolderLookup.Provider registries, DATA data) {
                return codec.encodeStart(ops, data).getOrThrow();
            }

            @Override
            public DATA deserialize(HolderLookup.Provider registries, CONTAINER container) {
                return codec.decode(ops, container).getOrThrow().getFirst();
            }
        };
    }
}
