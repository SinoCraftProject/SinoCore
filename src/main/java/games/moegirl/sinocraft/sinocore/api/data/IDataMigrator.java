package games.moegirl.sinocraft.sinocore.api.data;

/**
 * Data migrator between 2 data versioning. <br/>
 * For upgrading and downgrading data.
 *
 * @param <SELF>   Data type itself.
 * @param <TARGET> The target data type.
 */
public interface IDataMigrator<SELF, TARGET> {
    TARGET migrate(SELF data);
}
