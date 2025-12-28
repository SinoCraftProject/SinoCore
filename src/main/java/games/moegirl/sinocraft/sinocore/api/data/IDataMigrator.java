package games.moegirl.sinocraft.sinocore.api.data;

/**
 * Data migrator for data versioning.
 *
 * @param <SELF> Data type itself.
 * @param <UP>   Upgraded data type.
 * @param <DOWN> Downgraded data type.
 */
public interface IDataMigrator<SELF, UP, DOWN> {
    UP up(SELF data);

    DOWN down(SELF data);
}
