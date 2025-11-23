package games.moegirl.sinocraft.sinocore.data.gen.model;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

@Deprecated(forRemoval = true, since = "1.2.0")
public interface IItemModelBuilder<T extends IItemModelBuilder<T>> extends IModelBuilder<T> {

    IOverrideBuilder<T> override();

    IOverrideBuilder<T> override(int index);

    JsonObject toJson();

    interface IOverrideBuilder<T> {

        IOverrideBuilder<T> model(IModelFile model);

        IOverrideBuilder<T> predicate(ResourceLocation key, float value);

        T end();
    }
}
