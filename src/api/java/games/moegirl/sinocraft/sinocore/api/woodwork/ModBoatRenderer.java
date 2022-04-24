package games.moegirl.sinocraft.sinocore.api.woodwork;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ModBoatRenderer extends EntityRenderer<ModBoat> {

    private final EntityRendererProvider.Context context;
    private final Map<Woodwork, Pair<ResourceLocation, BoatModel>> woodworkPairHashMap = new HashMap<>();
    private final Pair<ResourceLocation, BoatModel> oak;

    public ModBoatRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.context = context;
        this.shadowRadius = 0.8F;
        this.oak = Pair.of(new ResourceLocation("textures/entity/boat/" + Boat.Type.OAK.getName() + ".png"),
                new BoatModel(context.bakeLayer(ModelLayers.createBoatModelName(Boat.Type.OAK))));
    }

    public void render(ModBoat pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.0D, 0.375D, 0.0D);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - pEntityYaw));
        float f = (float) pEntity.getHurtTime() - pPartialTicks;
        float f1 = pEntity.getDamage() - pPartialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float) pEntity.getHurtDir()));
        }

        float f2 = pEntity.getBubbleAngle(pPartialTicks);
        if (!Mth.equal(f2, 0.0F)) {
            pMatrixStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), pEntity.getBubbleAngle(pPartialTicks), true));
        }

        Pair<ResourceLocation, BoatModel> pair = getModelWithLocation(pEntity);
        ResourceLocation resourcelocation = pair.getFirst();
        BoatModel boatmodel = pair.getSecond();
        pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        boatmodel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(boatmodel.renderType(resourcelocation));
        boatmodel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!pEntity.isUnderWater()) {
            VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RenderType.waterMask());
            boatmodel.waterPatch().render(pMatrixStack, vertexconsumer1, pPackedLight, OverlayTexture.NO_OVERLAY);
        }

        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Deprecated // forge: override getModelWithLocation to change the texture / model
    public ResourceLocation getTextureLocation(ModBoat pEntity) {
        return getModelWithLocation(pEntity).getFirst();
    }

    public Pair<ResourceLocation, BoatModel> getModelWithLocation(ModBoat boat) {
        return boat.getWoodwork().map(woodwork -> woodworkPairHashMap.computeIfAbsent(woodwork, n -> {
            ResourceLocation entity = new ResourceLocation(woodwork.name.getNamespace(),
                    "textures/entity/boat/" + woodwork.name.getPath() + ".png");
            return Pair.of(entity, new BoatModel(context.bakeLayer(woodwork.boatLayer())));
        })).orElse(oak);
    }

    public static ModelLayerLocation registerLayer(Woodwork woodwork, EntityRenderersEvent.RegisterLayerDefinitions event) {
        ResourceLocation name = woodwork.name();
        ResourceLocation location = new ResourceLocation(name.getNamespace(), "boat/" + name.getPath());
        ModelLayerLocation boatLayer = new ModelLayerLocation(location, "main");
        event.registerLayerDefinition(boatLayer, BoatModel::createBodyModel);
        return boatLayer;
    }
}
