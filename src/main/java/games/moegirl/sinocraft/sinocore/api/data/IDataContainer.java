package games.moegirl.sinocraft.sinocore.api.data;

import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;

import java.util.Map;

public interface IDataContainer<HOLDER extends ISinoDataHolder> {
    <DATA> void set(DataType<DATA> type, DATA data);

    <DATA> DATA get(DataType<DATA> type);

    <DATA> boolean has(DataType<DATA> type);

    <DATA> void remove(DataType<DATA> type);

    Map<DataType<Object>, Object> getAll();
}
