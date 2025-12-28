package games.moegirl.sinocraft.sinocore.api.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Sync data with stream buffer.
 * <p>
 * It is used for network data transmission.
 *
 * @param <DATA> Data type.
 * @param <BUF> Buffer type, usually {@link RegistryFriendlyByteBuf}.
 */
public interface IDataSyncer<DATA, BUF extends ByteBuf> {
    void write(BUF buf, DATA data);

    DATA read(BUF buf);
}
