package games.moegirl.sinocraft.sinocore.datagen.model;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class UncheckedExistingModelFile extends ModelFile.ExistingModelFile {
    private final Consumer<ResourceLocation> onNotExist;

    public UncheckedExistingModelFile(ResourceLocation location, ExistingFileHelper existingHelper, Consumer<ResourceLocation> onNotExist) {
        super(location, existingHelper);
        this.onNotExist = onNotExist;
    }

    @Override
    protected boolean exists() {
        var exists = super.exists();
        if (!exists) {
            onNotExist.accept(this.location);
        }
        return true;
    }
}
