package games.moegirl.sinocraft.sinocore.api.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Serialize data from/to stream buffer.
 * <p>
 * It is used for network data transmission.
 *
 * @param <DATA> Data type.
 * @param <BUF>  Buffer type, usually {@link RegistryFriendlyByteBuf}.
 */
public interface IDataStreamSerializer<DATA, BUF extends FriendlyByteBuf> {
    void write(BUF buf, DATA data);

    DATA read(BUF buf);

    static <DATA, BUF extends FriendlyByteBuf> IDataStreamSerializer<DATA, BUF> fromStreamCodec(StreamCodec<BUF, DATA> codec) {
        return new IDataStreamSerializer<DATA, BUF>() {
            @Override
            public void write(BUF buf, DATA data) {
                codec.encode(buf, data);
            }

            @Override
            public DATA read(BUF buf) {
                return codec.decode(buf);
            }
        };
    }
}
