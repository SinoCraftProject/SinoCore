package games.moegirl.sinocraft.sinocore.api.data;

import games.moegirl.sinocraft.sinocore.api.injectable.ISinoDataHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class DataType<DATA, HOLDER extends ISinoDataHolder> {
    private final ResourceLocation id;
    private final int version;
    private final IDataCreator<DATA, HOLDER> factory;

    public DATA create(HOLDER holder) {
        return factory.create(holder);
    }

    @Override
    public int hashCode() { // We assume that (id + version) is unique
        return Objects.hash(id.hashCode(), version);
    }
}
