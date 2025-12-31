package games.moegirl.sinocraft.sinocore.api.data;

import net.minecraft.resources.ResourceLocation;

/**
 * We consider (id + version) as unique key
 *
 * @param id      Data ID
 * @param version Data version
 */
public record DataKey(ResourceLocation id, int version) {

    /**
     * When two data keys have same id, we called they are same, version is ignored.
     *
     * @param other Other DataKey
     * @return True if same ID
     */
    public boolean isSame(DataKey other) {
        return id.equals(other.id);
    }
}
