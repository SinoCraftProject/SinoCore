package games.moegirl.sinocraft.sinocore.datagen.model;

import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class LenientItemModelBuilder extends ItemModelBuilder {
    private final Consumer<ResourceLocation> onNotExist;

        public LenientItemModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper, Consumer<ResourceLocation> onNotExist) {
        super(outputLocation, existingFileHelper);
        this.onNotExist = onNotExist;
    }

    @Override
    public ItemModelBuilder texture(String key, ResourceLocation texture) {
        Preconditions.checkNotNull(key, "Key must not be null");
        Preconditions.checkNotNull(texture, "Texture must not be null");
        if (!existingFileHelper.exists(texture, ModelProvider.TEXTURE)) {
            onNotExist.accept(texture);
        }
        this.textures.put(key, texture.toString());
        return this;
    }
}
