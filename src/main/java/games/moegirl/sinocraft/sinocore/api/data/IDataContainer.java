package games.moegirl.sinocraft.sinocore.api.data;

import java.util.Map;

public interface IDataContainer {
    <DATA> void set(DataType<DATA> type, DATA data);

    <DATA> DATA get(DataType<DATA> type);

    <DATA> boolean has(DataType<DATA> type);

    <DATA> void remove(DataType<DATA> type);

    Map<DataType<?>, ?> getAll();
}
