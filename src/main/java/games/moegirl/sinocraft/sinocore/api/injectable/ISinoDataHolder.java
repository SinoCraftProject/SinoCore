package games.moegirl.sinocraft.sinocore.api.injectable;

import games.moegirl.sinocraft.sinocore.api.data.DataType;

import java.util.Map;

public interface ISinoDataHolder {
    <DATA> void sino$setData(DataType<DATA> type, DATA data);

    <DATA> DATA sino$getData(DataType<DATA> type);

    <DATA> boolean sino$hasData(DataType<DATA> type);

    <DATA> void sino$removeData(DataType<DATA> type);

    Map<DataType<Object>, Object> sino$getAllData();
}
