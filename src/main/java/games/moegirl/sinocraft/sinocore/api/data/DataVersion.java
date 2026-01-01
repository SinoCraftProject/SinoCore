package games.moegirl.sinocraft.sinocore.api.data;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class DataVersion<DATA> {
    private final Class<DATA> clazz;

    /**
     * Codec for this data version. <br/>
     * {@code null} for transient data.
     */
    @Nullable
    private Codec<DATA> codec;

    /**
     * Whether this data version can be saved. <br/>
     * {@code false} for decode only.
     */
    private boolean savable = false;

    /**
     * Stream codec for this data version. <br/>
     * {@code null} for it won't be sync to client.
     */
    @Nullable
    private StreamCodec<RegistryFriendlyByteBuf, DATA> streamCodec;

    /**
     * Data migrator from previous version to this version. <br/>
     * {@code null} for no migrator.
     */
    @Nullable
    private IDataMigrator<?, DATA> migrator;
}
