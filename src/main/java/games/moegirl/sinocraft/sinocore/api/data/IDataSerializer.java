package games.moegirl.sinocraft.sinocore.api.data;

import com.google.gson.JsonElement;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

/**
 * Serialize data object from/to structured object (e.g. {@link CompoundTag}, {@link JsonElement}).
 * <p>
 * It is used for data storage.
 *
 * @param <DATA> Data type.
 * @param <STRUCTURED> Structured data type.
 */
public interface IDataSerializer<DATA, STRUCTURED> {
    STRUCTURED serialize(HolderLookup.Provider registries, DATA data);

    DATA deserialize(HolderLookup.Provider registries, STRUCTURED structured);
}
