package games.moegirl.sinocraft.sinocore.api.data;

/**
 * Copy data to another instance.
 * <p>
 * It is used for data cloning on player death.
 *
 * @param <DATA> Data type.
 */
@FunctionalInterface
public interface IDataCopier<DATA> {
    DATA copy(DATA data);
}
