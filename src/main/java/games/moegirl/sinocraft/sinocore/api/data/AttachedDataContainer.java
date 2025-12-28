package games.moegirl.sinocraft.sinocore.api.data;

import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;

import java.util.Map;

public class AttachedDataContainer<HOLDER extends ISinoDataHolder> implements IDataContainer<HOLDER> {
    @Override
    public <DATA> void set(DataType<DATA, HOLDER> type, DATA data) {

    }

    @Override
    public <DATA> DATA get(DataType<DATA, HOLDER> type) {
        return null;
    }

    @Override
    public <DATA> boolean has(DataType<DATA, HOLDER> type) {
        return false;
    }

    @Override
    public <DATA> void remove(DataType<DATA, HOLDER> type) {

    }

    @Override
    public Map<DataType<Object, HOLDER>, Object> getAll() {
        return Map.of();
    }
}
