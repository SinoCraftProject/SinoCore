package games.moegirl.sinocraft.sinocore.api.event.client.args.render;

import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocore.api.event.CancellableArgsBase;
import lombok.Getter;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;

public class RenderItemInFrameArgs extends CancellableArgsBase {
    @Getter
    private final ItemStack itemStack;

    @Getter
    private final ItemFrame itemFrameEntity;

    @Getter
    private final ItemFrameRenderer<?> renderer;

    @Getter
    private final PoseStack poseStack;

    @Getter
    private final MultiBufferSource multiBufferSource;

    @Getter
    private final int packedLight;

    public RenderItemInFrameArgs(ItemStack itemStack, ItemFrame itemFrameEntity, ItemFrameRenderer<?> renderer,
                                 PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        this.itemStack = itemStack;
        this.itemFrameEntity = itemFrameEntity;
        this.renderer = renderer;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.packedLight = packedLight;
    }
}
