package games.moegirl.sinocraft.sinocore.api.data;

import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;

/**
 * Create data instance with target holder.
 * <p>
 * Holder types (default supported):<br/>
 * {@link Entity},
 * {@link BlockEntity},
 * {@link ItemStack},
 * {@link ChunkAccess},
 * {@link Level}
 *
 * @param <DATA>   Data type.
 * @param <HOLDER> Holder type.
 */
@FunctionalInterface
public interface IDataCreator<DATA, HOLDER extends ISinoDataHolder> {
    DATA create(HOLDER holder);
}
