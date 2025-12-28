package games.moegirl.sinocraft.sinocore.api.data;

import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;

import java.util.Map;

public interface IDataContainer<HOLDER extends ISinoDataHolder> {
    <DATA> void set(DataType<DATA, HOLDER> type, DATA data);

    <DATA> DATA get(DataType<DATA, HOLDER> type);

    <DATA> boolean has(DataType<DATA, HOLDER> type);

    <DATA> void remove(DataType<DATA, HOLDER> type);

    Map<DataType<Object, HOLDER>, Object> getAll();
}
