package games.moegirl.sinocraft.sinocore.api.data;

import net.minecraft.resources.ResourceLocation;

/**
 * We consider (id + version) as unique key
 *
 * @param id      Data ID
 * @param version Data version
 */
public record DataKey(ResourceLocation id, int version) {
}
