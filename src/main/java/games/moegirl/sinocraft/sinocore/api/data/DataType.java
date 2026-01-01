package games.moegirl.sinocraft.sinocore.api.data;

import com.mojang.serialization.Codec;
import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class DataType {
    private final ResourceLocation id;

    private final boolean saved;
    private final boolean synced;

    private final Map<Integer, DataVersion<?>> versions;

    private final List<Class<? extends ISinoDataHolder>> appliesTo;

    public boolean is(ResourceLocation id, int version) {
        return is(new DataKey(id, version));
    }

    public boolean is(DataKey version) {
        return key.equals(version);
    }

    public boolean canBeSaved() {
        return this.saved;
    }

    public boolean canBeSynced() {
        return this.synced;
    }

    public boolean isAvailableFor(ISinoDataHolder holder) {
        return isAvailableFor(holder.getClass());
    }

    public boolean isAvailableFor(Class<? extends ISinoDataHolder> holderClass) {
        return appliesTo.stream().anyMatch(holderClass::isAssignableFrom);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
