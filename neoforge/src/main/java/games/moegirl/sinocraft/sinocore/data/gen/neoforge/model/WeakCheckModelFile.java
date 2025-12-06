package games.moegirl.sinocraft.sinocore.data.gen.neoforge.model;

import games.moegirl.sinocraft.sinocore.neoforge.api.datagen.model.UncheckedExistingModelFile;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Function;

/**
 * Model file with weak check.
 * Use {@link UncheckedExistingModelFile} instead.
 */
@Deprecated(forRemoval = true, since = "1.2.0")
public class WeakCheckModelFile extends ModelFile.ExistingModelFile {

    protected final boolean strict;

    protected final Function<ResourceLocation, Boolean> onNotExists;

    public WeakCheckModelFile(ResourceLocation location, ExistingFileHelper existingHelper, boolean strict,
                              Function<ResourceLocation, Boolean> onNotExists) {
        super(location, existingHelper);

        this.strict = strict;
        this.onNotExists = onNotExists;
    }

    @Override
    protected boolean exists() {
        var result = super.exists();
        if (strict && !result) {
            return false;
        } else {
            return onNotExists.apply(getUncheckedLocation());
        }
    }
}
