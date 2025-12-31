package games.moegirl.sinocraft.sinocore.api.data;

import com.mojang.serialization.Codec;
import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class DataType<DATA> {
    private final Class<DATA> clazz;
    private final DataKey key;

    @Nullable
    private Codec<DATA> codec;

    @Nullable
    private StreamCodec<RegistryFriendlyByteBuf, DATA> streamCodec;

    @Nullable
    private DataType<?> migratesTo;
    @Nullable
    private IDataMigrator<DATA, ?> migrator;

    private final List<Class<? extends ISinoDataHolder>> appliesTo;

    public boolean is(ResourceLocation id, int version) {
        return is(new DataKey(id, version));
    }

    public boolean is(DataKey version) {
        return key.equals(version);
    }

    public boolean canBeSaved() {
        return codec != null;
    }

    public boolean canBeSynced() {
        return streamCodec != null;
    }

    public boolean isAvailableFor(ISinoDataHolder holder) {
        return isAvailableFor(holder.getClass());
    }

    public boolean isAvailableFor(Class<? extends ISinoDataHolder> holderClass) {
        return appliesTo.stream().anyMatch(holderClass::isAssignableFrom);
    }

    @Override
    public int hashCode() { // Use the hash code of DataKey.
        return key.hashCode();
    }

    public static class Builder<DATA> {
        private final Class<DATA> clazz;
        private final DataKey key;

        @Nullable
        private Codec<DATA> codec;

        @Nullable
        private StreamCodec<RegistryFriendlyByteBuf, DATA> streamCodec;

        @Nullable
        private DataType<?> migratesTo;
        @Nullable
        private IDataMigrator<DATA, ?> migrator;

        private final List<Class<? extends ISinoDataHolder>> appliesTo = new ArrayList<>();

        public Builder(Class<DATA> clazz, ResourceLocation id, int version) {
            this.clazz = clazz;
            this.key = new DataKey(id, version);
        }

        public Builder<DATA> codec(Codec<DATA> codec) {
            this.codec = codec;
            return this;
        }

        public Builder<DATA> streamCodec(StreamCodec<RegistryFriendlyByteBuf, DATA> streamCodec) {
            this.streamCodec = streamCodec;
            return this;
        }

        public <TARGET> Builder<DATA> migratesTo(DataType<TARGET> type, IDataMigrator<DATA, TARGET> migrator) {
            this.migratesTo = type;
            this.migrator = migrator;
            return this;
        }

        /**
         * Holder types (default supported):<br/>
         * {@link Entity},
         * {@link BlockEntity},
         * {@link ItemStack},
         * {@link ChunkAccess},
         * {@link Level}
         *
         * @param holderClass Holder type.
         * @return this
         */
        public Builder<DATA> appliesTo(Class<ISinoDataHolder> holderClass) {
            this.appliesTo.add(holderClass);
            return this;
        }

        public DataType<DATA> build() {
            return new DataType<>(clazz, key, codec, streamCodec, migratesTo, migrator, appliesTo);
        }
    }
}
