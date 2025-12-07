package games.moegirl.sinocraft.sinocore.api.event.client.args.model;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class AfterBakeArgs {
    @Getter
    private final ModelBakery modelBakery;

    @Getter
    private final ModelResourceLocation id;

    @Getter
    @Setter
    private BakedModel model;

    public AfterBakeArgs(ModelBakery modelBakery, ModelResourceLocation id, BakedModel model) {
        this.modelBakery = modelBakery;
        this.id = id;
        this.model = model;
    }
}
