package games.moegirl.sinocraft.sinocore.data.migratable;

/**
 * Interface for migrate-able data class.
 * @param <UPPER> Upper version data class.
 * @param <LOWER> Lower version data class.
 * <p>
 * Will move to {@code games.moegirl.sinocraft.sinocore.api.data} in 1.2.0.
 */
public interface IDataMigratable<UPPER, LOWER> {
    UPPER up();

    LOWER down();
}
