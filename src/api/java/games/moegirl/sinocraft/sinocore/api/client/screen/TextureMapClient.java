package games.moegirl.sinocraft.sinocore.api.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import games.moegirl.sinocraft.sinocore.api.client.GLSwitcher;
import games.moegirl.sinocraft.sinocore.api.client.screen.component.ImageButton;
import games.moegirl.sinocraft.sinocore.api.utility.texture.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("unused")
public class TextureMapClient {

    private final TextureMap texture;
    private final RenderTypes render;

    public TextureMapClient(TextureMap texture) {
        this.texture = texture;
        this.render = new RenderTypes(texture.texture());
    }

    public Optional<Button> placeButton(String name,
                                        AbstractContainerScreen<?> parent,
                                        Button.OnPress onPress,
                                        @Nullable Button.OnPress onRightPress,
                                        Function<Button, Button> addRenderableWidget) {
        return texture.buttons().get(name)
                .map(e -> addRenderableWidget.apply(new ImageButton(parent, texture, e, onPress, onRightPress)));
    }

    public Optional<Button> placeButton(String name,
                                        AbstractContainerScreen<?> parent,
                                        Button.OnPress onPress,
                                        Function<Button, Button> addRenderableWidget) {
        return placeButton(name, parent, onPress, null, addRenderableWidget);
    }

    public void renderText(PoseStack stack, String name) {
        renderText(stack, Minecraft.getInstance().font, name);
    }

    public void renderText(PoseStack stack, Font font, String name) {
        texture.texts().get(name).ifPresent(entry -> {
            if (entry.text() == null) {
                if (entry.rawText() != null) {
                    renderText(stack, entry, entry.rawText(), font);
                }
            } else {
                renderText(stack, entry, new TranslatableComponent(entry.text()), font);
            }
        });
    }

    public void renderText(PoseStack stack, AbstractContainerScreen<?> parent, String name) {
        renderText(stack, parent, Minecraft.getInstance().font, name);
    }

    public void renderText(PoseStack stack, AbstractContainerScreen<?> parent, Font font, String name) {
        texture.texts().get(name).ifPresent(entry -> {
            if (entry.text() == null) {
                if (entry.rawText() == null) {
                    renderText(stack, entry, parent.getTitle(), font);
                } else {
                    renderText(stack, entry, entry.rawText(), font);
                }
            } else {
                renderText(stack, entry, new TranslatableComponent(entry.text()), font);
            }
        });
    }

    private void renderText(PoseStack stack, TextEntry entry, Component text, Font font) {
        int tx = entry.x();
        int ty = entry.y();
        if (entry.center()) {
            tx += font.width(text) / 2;
        }
        if (entry.shadow()) {
            font.drawShadow(stack, text, tx, ty, entry.color());
        } else {
            font.draw(stack, text, tx, ty, entry.color());
        }
    }

    private void renderText(PoseStack stack, TextEntry entry, String text, Font font) {
        int tx = entry.x();
        int ty = entry.y();
        if (entry.center()) {
            tx += font.width(text) / 2;
        }
        if (entry.shadow()) {
            font.drawShadow(stack, text, tx, ty, entry.color());
        } else {
            font.draw(stack, text, tx, ty, entry.color());
        }
    }

    public void bindTexture() {
        RenderSystem.setShaderTexture(0, texture.texture());
    }

    public void blitTexture(PoseStack stack, String name, AbstractContainerScreen<?> gui, GLSwitcher... configurations) {
        texture.textures().get(name).ifPresent(entry -> {
            bindTexture();
            blitTexture(stack, gui.getGuiLeft() + entry.x(), gui.getGuiTop() + entry.y(), entry.w(), entry.h(),
                    entry.u(), entry.v(), entry.tw(), entry.th());
        });
        resumeGL(configurations);
    }

    public void blitTexture(PoseStack stack, String name, int x, int y, AbstractContainerScreen<?> gui, GLSwitcher... configurations) {
        texture.textures().get(name).ifPresent(entry -> {
            bindTexture();
            blitTexture(stack, gui.getGuiLeft() + x, gui.getGuiTop() + y, entry.w(), entry.h(),
                    entry.u(), entry.v(), entry.tw(), entry.th());
        });
        resumeGL(configurations);
    }

    public void blitTexture(PoseStack stack, String name, String position, AbstractContainerScreen<?> gui, GLSwitcher... configurations) {
        texture.textures().get(name).ifPresent(entry -> {
            texture.points().get(position).ifPresent(position -> {
                bindTexture();
                blitTexture(stack, gui.getGuiLeft() + position.x(), gui.getGuiTop() + position.y(), entry.w(), entry.h(),
                        entry.u(), entry.v(), entry.tw(), entry.th());
            });
        });
        resumeGL(configurations);
    }

    public void blitProgress(PoseStack stack, String name,
                             AbstractContainerScreen<?> gui,
                             float progress, GLSwitcher... configurations) {
        texture.progress().get(name).ifPresent(entry -> {
            bindTexture();
            int x = gui.getGuiLeft() + entry.x();
            int y = gui.getGuiTop() + entry.y();
            String begin = entry.texture();
            int finalX = x;
            int finalY = y;
            texture.textures().get(begin).ifPresent(bg ->
                    blitTexture(stack, finalX, finalY, bg.w(), bg.h(), bg.u(), bg.v(), bg.tw(), bg.th()));
            if (progress > 0) {
                TextureEntry p = texture.textures().ensureGet(entry.textureFilled());
                int w = p.w(), h = p.h();
                float tu = p.u(), tv = p.v();
                int tw = p.tw(), th = p.th();
                if (entry.isVertical()) {
                    if (entry.isOpposite()) {
                        tv += (1 - progress) * p.th();
                        y += (1 - progress) * p.y();
                    }
                    th = (int) (th * progress);
                } else {
                    if (entry.isOpposite()) {
                        tu += (1 - progress) * p.tw();
                        x += (1 - progress) * p.w();
                    }
                    tw = (int) (tw * progress);
                }
                blitTexture(stack, x, y, w, h, tu, tv, tw, th);
            }
        });
        resumeGL(configurations);
    }

    private void blitTexture(PoseStack stack, int x, int y, int w, int h, float tu, float tv, int tw, int th) {
        if (w > 0 && h > 0) {
            GuiComponent.blit(stack, x, y, w, h, tu, tv, tw, th, texture.width, texture.height);
        }
    }

    public void blitTexture(MultiBufferSource bufferSource, PoseStack poseStack, String name,
                            boolean transparency, Rect rect) {
        texture.textures().get(name).ifPresent(entry -> {
            bindTexture();
            Matrix4f matrix = poseStack.last().pose();
            RenderType type = transparency ? render.renderTypeWithTransparency.get() : render.renderType.get();
            VertexConsumer buffer = bufferSource.getBuffer(type);
            float[] uv = entry.normalized(texture.width, texture.height);
            buffer.vertex(matrix, rect.x0(), rect.y0(), rect.z0()).uv(uv[0], uv[2]).endVertex();
            buffer.vertex(matrix, rect.x1(), rect.y1(), rect.z1()).uv(uv[0], uv[3]).endVertex();
            buffer.vertex(matrix, rect.x2(), rect.y2(), rect.z2()).uv(uv[1], uv[3]).endVertex();
            buffer.vertex(matrix, rect.x3(), rect.y3(), rect.z3()).uv(uv[1], uv[2]).endVertex();
        });
    }

    public void blitTexture(MultiBufferSource bufferSource, PoseStack poseStack, String name,
                            boolean transparency, Rect rect, int packedLight) {
        blitTexture(bufferSource, poseStack, name, transparency, rect, packedLight, 1, 1, 1, 1);
    }

    public void blitTexture(MultiBufferSource bufferSource, PoseStack poseStack, String name,
                            boolean transparency, Rect rect, float r, float g, float b, float a) {
        texture.textures().get(name).ifPresent(entry -> {
            bindTexture();
            Matrix4f matrix = poseStack.last().pose();
            RenderType type = transparency ? render.renderTypeWithColorTransparency.get() : render.renderTypeWithColor.get();
            VertexConsumer buffer = bufferSource.getBuffer(type);
            float[] uv = entry.normalized(texture.width, texture.height);
            buffer.vertex(matrix, rect.x0(), rect.y0(), rect.z0()).color(r, g, b, a).uv(uv[0], uv[2]).endVertex();
            buffer.vertex(matrix, rect.x1(), rect.y1(), rect.z1()).color(r, g, b, a).uv(uv[0], uv[3]).endVertex();
            buffer.vertex(matrix, rect.x2(), rect.y2(), rect.z2()).color(r, g, b, a).uv(uv[1], uv[3]).endVertex();
            buffer.vertex(matrix, rect.x3(), rect.y3(), rect.z3()).color(r, g, b, a).uv(uv[1], uv[2]).endVertex();
        });
    }

    public void blitTexture(MultiBufferSource bufferSource, PoseStack poseStack, String name,
                            boolean transparency, Rect rect, int packedLight, float r, float g, float b, float a) {
        texture.textures().get(name).ifPresent(entry -> {
            bindTexture();
            Matrix4f matrix = poseStack.last().pose();
            RenderType type = transparency ? render.renderTypeWithColorLightmapTransparency.get() : render.renderTypeWithColorLightmap.get();
            VertexConsumer buffer = bufferSource.getBuffer(type);
            float[] uv = entry.normalized(texture.width, texture.height);
            buffer.vertex(matrix, rect.x0(), rect.y0(), rect.z0()).color(r, g, b, a).uv(uv[0], uv[2]).uv2(packedLight).endVertex();
            buffer.vertex(matrix, rect.x1(), rect.y1(), rect.z1()).color(r, g, b, a).uv(uv[0], uv[3]).uv2(packedLight).endVertex();
            buffer.vertex(matrix, rect.x2(), rect.y2(), rect.z2()).color(r, g, b, a).uv(uv[1], uv[3]).uv2(packedLight).endVertex();
            buffer.vertex(matrix, rect.x3(), rect.y3(), rect.z3()).color(r, g, b, a).uv(uv[1], uv[2]).uv2(packedLight).endVertex();
        });
    }

    private void resumeGL(GLSwitcher... configurations) {
        for (var switcher : configurations) {
            switcher.resume();
        }
    }

}
