package games.moegirl.sinocraft.sinocore.api.data;

import com.mojang.serialization.Codec;
import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DataTypeBuilder {
    private final ResourceLocation id;

    private final List<Class<? extends ISinoDataHolder>> appliesTo = new ArrayList<>();

    private final Map<Integer, DataVersion<?>> versions = new LinkedHashMap<>();

    private boolean saved = false;
    private boolean synced = false;
    private int latestVersion = 1;

    public DataTypeBuilder(ResourceLocation id) {
        this.id = id;
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
    public DataTypeBuilder appliesTo(Class<ISinoDataHolder> holderClass) {
        this.appliesTo.add(holderClass);
        return this;
    }

    public <DATA> DataVersionBuilder<DATA> version(Class<DATA> clazz) {
        return version(clazz, 1);
    }

    public <DATA> DataVersionBuilder<DATA> nextVersion(Class<DATA> clazz) {
        return version(clazz, latestVersion + 1);
    }

    public <DATA> DataVersionBuilder<DATA> version(Class<DATA> clazz, int version) {
        return new DataVersionBuilder<>(this, clazz, version);
    }

    public DataType build() {
        return new DataType(clazz, key, codec, streamCodec, migratesTo, migrator, appliesTo);
    }

    private <DATA> DataTypeBuilder addVersion(DataVersionBuilder<DATA> builder) {
        var version = builder.version;
        if (this.latestVersion < version) {
            this.latestVersion = version;
        }

        if (this.saved) {
            if (!builder.savable) {
                throw new IllegalArgumentException("Version " + version + " of data type " + id + " should be saved.");
            }
            Objects.requireNonNull(builder.codec, "Version " + version + " of data type " + id + " must have a codec as savable.");
        } else {
            if (builder.savable) {
                this.saved = true;
            }
        }

        this.versions.put(version, builder.build());
        return this;
    }

    public static class DataVersionBuilder<DATA> {
        protected final DataTypeBuilder parent;

        protected final Class<DATA> clazz;
        protected final int version;

        @Nullable
        protected Codec<DATA> codec;
        protected boolean savable = false;

        @Nullable
        protected StreamCodec<RegistryFriendlyByteBuf, DATA> streamCodec;

        public DataVersionBuilder(DataTypeBuilder parent, Class<DATA> clazz, int version) {
            this.parent = parent;
            this.clazz = clazz;
            this.version = version;
        }

        /**
         * Set codec for this version. Savable by default.
         *
         * @param codec Codec.
         * @return this
         */
        public DataVersionBuilder<DATA> codec(Codec<DATA> codec) {
            return codec(codec, true);
        }

        /**
         * Set codec for this version. You may set savable to false if you want to use it for decode only.
         *
         * @param codec   Codec.
         * @param savable Savable.
         * @return this
         */
        public DataVersionBuilder<DATA> codec(Codec<DATA> codec, boolean savable) {
            this.codec = codec;
            this.savable = savable;
            return this;
        }

        public DataVersionBuilder<DATA> streamCodec(StreamCodec<RegistryFriendlyByteBuf, DATA> streamCodec) {
            this.streamCodec = streamCodec;
            return this;
        }

        public DataTypeBuilder end() {
            return parent.addVersion(this);
        }

        protected DataVersion<DATA> build() {
            return new DataVersion<>(clazz, codec, savable, streamCodec, null);
        }
    }

    public static class MigratedBuilder<DATA, NEXT> extends DataVersionBuilder<NEXT> {
        protected final DataVersionBuilder<DATA> prev;

        @Nullable
        protected IDataMigrator<DATA, NEXT> migrator;

        public MigratedBuilder(DataVersionBuilder<DATA> prev, DataTypeBuilder parent, Class<NEXT> clazz, int version) {
            super(parent, clazz, version);
            this.prev = prev;
        }

        public MigratedBuilder<DATA, NEXT> migrator(IDataMigrator<DATA, NEXT> migrator) {
            this.migrator = migrator;
            return this;
        }

        @Override
        protected DataVersion<NEXT> build() {
            return new DataVersion<>(clazz, codec, savable, streamCodec, migrator);
        }
    }
}
