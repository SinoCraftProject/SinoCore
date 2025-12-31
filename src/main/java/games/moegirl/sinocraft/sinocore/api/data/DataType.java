package games.moegirl.sinocraft.sinocore.api.data;

import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Getter
@AllArgsConstructor
public class DataType<DATA> {
    private final DataKey key;

    @Nullable
    private IDataSerializer<DATA, CompoundTag> serializer;

    @Nullable
    private IDataStreamSerializer<DATA, RegistryFriendlyByteBuf> streamSerializer;

    private final Map<Predicate<DataKey>, IDataMigrator<?, DATA>> fromMigrators;
    private final List<Class<? extends ISinoDataHolder>> appliesTo;

    public boolean canMigrateFrom(ResourceLocation id, int version) {
        return canMigrateFrom(new DataKey(id, version));
    }

    public boolean canMigrateFrom(DataKey key) {
        return fromMigrators.keySet().stream().anyMatch(k -> k.test(key));
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
        private IDataSerializer<DATA, CompoundTag> serializer;

        @Nullable
        private IDataStreamSerializer<DATA, RegistryFriendlyByteBuf> streamSerializer;

        private final Map<Predicate<DataKey>, IDataMigrator<?, DATA>> fromMigrators = new HashMap<>();
        private final List<Class<? extends ISinoDataHolder>> appliesTo = new ArrayList<>();

        public Builder(Class<DATA> clazz, ResourceLocation id, int version) {
            this.clazz = clazz;
            this.key = new DataKey(id, version);
        }

        public Builder<DATA> serializer(IDataSerializer<DATA, CompoundTag> serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder<DATA> streamSerializer(IDataStreamSerializer<DATA, RegistryFriendlyByteBuf> streamSerializer) {
            this.streamSerializer = streamSerializer;
            return this;
        }

        public <FROM> Builder<DATA> canMigrateFrom(ResourceLocation id, int version, IDataMigrator<FROM, DATA> migrator) {
            return canMigrateFrom(new DataKey(id, version), migrator);
        }

        public <FROM> Builder<DATA> canMigrateFrom(DataKey key, IDataMigrator<FROM, DATA> migrator) {
            return canMigrateFrom(k -> k.equals(key), migrator);
        }

        public <FROM> Builder<DATA> canMigrateFrom(Predicate<DataKey> predicate, IDataMigrator<FROM, DATA> migrator) {
            this.fromMigrators.put(predicate, migrator);
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
            return new DataType<>(key, serializer, streamSerializer, fromMigrators, appliesTo);
        }
    }
}
