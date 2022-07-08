package games.moegirl.sinocraft.sinocore.api.utility.texture;

import com.google.common.collect.Iterators;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import games.moegirl.sinocraft.sinocore.api.utility.GLSwitcher;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public final class TextureMap {
    private final ResourceLocation texture;
    int width = 0;
    int height = 0;
    private final Entry<PointEntry> points = new Entry<>();
    private final Entry<TextEntry> texts = new Entry<>();
    private final Entry<TextureEntry> textures = new Entry<>();
    private final Entry<SlotEntry> slot = new Entry<>();
    private final Entry<SlotsEntry> slots = new Entry<>();
    private final Entry<ProgressEntry> progress = new Entry<>();
    private final Entry<ButtonEntry> buttons = new Entry<>();
    private final Lazy<RenderType> renderType,
            renderTypeWithColor,
            renderTypeWithTransparency,
            renderTypeWithColorTransparency,
            renderTypeWithColorLightmap,
            renderTypeWithColorLightmapTransparency;

    TextureMap(ResourceLocation texture) {
        this.texture = texture;
        renderType = Lazy.of(() -> {
            RenderType.CompositeState state = RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_TEX_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                    .createCompositeState(false);
            return RenderType.create(texture.toString(), DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS,
                    256, false, false, state);
        });
        renderTypeWithColor = Lazy.of(() -> {
            RenderType.CompositeState state = RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                    .createCompositeState(false);
            return RenderType.create(texture.toString(), DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS,
                    256, false, false, state);
        });
        renderTypeWithTransparency = Lazy.of(() -> {
            RenderType.CompositeState state = RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_TEX_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .createCompositeState(false);
            return RenderType.create(texture.toString(), DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS,
                    256, false, false, state);
        });
        renderTypeWithColorTransparency = Lazy.of(() -> {
            RenderType.CompositeState state = RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .createCompositeState(false);
            return RenderType.create(texture.toString(), DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS,
                    256, false, false, state);
        });
        renderTypeWithColorLightmap = Lazy.of(() -> {
            RenderType.CompositeState state = RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                    .createCompositeState(false);
            return RenderType.create(texture.toString(), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS,
                    256, false, false, state);
        });
        renderTypeWithColorLightmapTransparency = Lazy.of(() -> {
            RenderType.CompositeState state = RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .createCompositeState(false);
            return RenderType.create(texture.toString(), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS,
                    256, false, false, state);
        });
    }

    public static TextureMap of(ResourceLocation texture) {
        return TextureParser.parse(texture);
    }

    public static TextureMap of(String modid, String path, String name) {
        return TextureParser.parse(new ResourceLocation(modid, "textures/" + path + "/" + name + ".png"));
    }

    public <T extends Slot, C extends Container> Optional<T> placeSlot(C container, String name, int index, Function<Slot, Slot> addSlot, SlotStrategy<T, C> slot) {
        return this.slot.get(name)
                .map(entry -> placeSlot(container, entry, index, addSlot, slot));
    }

    public void placeSlots(Container container, String name, int beginIndex, Function<Slot, Slot> addSlot, SlotStrategy<Slot, Container> slot) {
        this.slots.get(name).ifPresent(slotsEntry -> {
            int i = beginIndex;
            for (SlotEntry entry : slotsEntry.entries()) {
                placeSlot(container, entry, i++, addSlot, slot);
            }
        });
    }

    private <T extends Slot, C extends Container> T placeSlot(C container, SlotEntry entry, int index, Function<Slot, Slot> addSlot, SlotStrategy<T, C> slot) {
        T s = slot.createSlot(container, index, entry.x(), entry.y());
        addSlot.apply(s);
        return s;
    }

    @OnlyIn(Dist.CLIENT)
    public Optional<Button> placeButton(String name,
                                        AbstractContainerScreen<?> parent,
                                        Button.OnPress onPress,
                                        @Nullable Button.OnPress onRightPress,
                                        Function<Button, Button> addRenderableWidget) {
        return buttons.get(name)
                .map(e -> addRenderableWidget.apply(new ImageButton(parent, this, e, onPress, onRightPress)));
    }

    @OnlyIn(Dist.CLIENT)
    public Optional<Button> placeButton(String name,
                                        AbstractContainerScreen<?> parent,
                                        Button.OnPress onPress,
                                        Function<Button, Button> addRenderableWidget) {
        return placeButton(name, parent, onPress, null, addRenderableWidget);
    }

    @OnlyIn(Dist.CLIENT)
    private void renderText(PoseStack stack, Font font, String name) {
        texts.get(name).ifPresent(entry -> {
            if (entry.text() == null) {
                if (entry.rawText() != null) {
                    int tx = entry.x();
                    int ty = entry.y();
                    String text = entry.rawText();
                    if (entry.center()) {
                        tx += font.width(text) / 2;
                    }
                    if (entry.shadow()) {
                        font.drawShadow(stack, text, tx, ty, entry.color());
                    } else {
                        font.draw(stack, text, tx, ty, entry.color());
                    }
                }
            } else {
                int tx = entry.x();
                int ty = entry.y();
                TranslatableComponent text = new TranslatableComponent(entry.text());
                if (entry.center()) {
                    tx += font.width(text) / 2;
                }
                if (entry.shadow()) {
                    font.drawShadow(stack, text, tx, ty, entry.color());
                } else {
                    font.draw(stack, text, tx, ty, entry.color());
                }
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void bindTexture() {
        RenderSystem.setShaderTexture(0, texture);
    }

    @OnlyIn(Dist.CLIENT)
    public void blitTexture(PoseStack stack, String name, AbstractContainerScreen<?> gui, GLSwitcher... configurations) {
        textures.get(name).ifPresent(entry -> {
            bindTexture();
            blitTexture(stack, gui.getGuiLeft() + entry.x(), gui.getGuiTop() + entry.y(), entry.w(), entry.h(),
                    entry.u(), entry.v(), entry.tw(), entry.th());
        });
        resumeGL(configurations);
    }

    @OnlyIn(Dist.CLIENT)
    public void blitProgress(PoseStack stack, String name,
                             AbstractContainerScreen<?> gui,
                             float progress, GLSwitcher... configurations) {
        this.progress.get(name).ifPresent(entry -> {
            bindTexture();
            int x = gui.getGuiLeft() + entry.x();
            int y = gui.getGuiTop() + entry.y();
            String begin = entry.texture();
            int finalX = x;
            int finalY = y;
            textures.get(begin).ifPresent(bg ->
                    blitTexture(stack, finalX, finalY, bg.w(), bg.h(), bg.u(), bg.v(), bg.tw(), bg.th()));
            if (progress > 0) {
                TextureEntry p = textures.ensureGet(entry.textureFilled());
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

    @OnlyIn(Dist.CLIENT)
    private void blitTexture(PoseStack stack, int x, int y, int w, int h, float tu, float tv, int tw, int th) {
        if (w > 0 && h > 0) {
            GuiComponent.blit(stack, x, y, w, h, tu, tv, tw, th, width, height);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void resumeGL(GLSwitcher... configurations) {
        for (var switcher : configurations) {
            switcher.resume();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void blitTexture(MultiBufferSource bufferSource, PoseStack poseStack, String name,
                            boolean transparency, float x, float y, float z, float dx, float dz) {
        textures.get(name).ifPresent(entry -> {
            bindTexture();
            Matrix4f matrix = poseStack.last().pose();
            RenderType type = transparency ? renderTypeWithTransparency.get() : renderType.get();
            VertexConsumer buffer = bufferSource.getBuffer(type);
            float[] uv = entry.normalized(width, height);
            buffer.vertex(matrix, x, y, z).uv(uv[0], uv[2]).endVertex();
            buffer.vertex(matrix, x, y, z + dz).uv(uv[0], uv[3]).endVertex();
            buffer.vertex(matrix, x + dx, y, z + dz).uv(uv[1], uv[3]).endVertex();
            buffer.vertex(matrix, x + dx, y, z).uv(uv[1], uv[2]).endVertex();
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void blitTexture(MultiBufferSource bufferSource, PoseStack poseStack, String name,
                            boolean transparency, float x, float y, float z, float dx, float dz, int packedLight) {
        blitTexture(bufferSource, poseStack, name, transparency, x, y, z, dx, dz, packedLight, 1, 1, 1, 1);
    }

    @OnlyIn(Dist.CLIENT)
    public void blitTexture(MultiBufferSource bufferSource, PoseStack poseStack, String name,
                            boolean transparency, float x, float y, float z, float dx, float dz, float r, float g, float b, float a) {
        textures.get(name).ifPresent(entry -> {
            bindTexture();
            Matrix4f matrix = poseStack.last().pose();
            RenderType type = transparency ? renderTypeWithColorTransparency.get() : renderTypeWithColor.get();
            VertexConsumer buffer = bufferSource.getBuffer(type);
            float[] uv = entry.normalized(width, height);
            buffer.vertex(matrix, x, y, z).color(r, g, b, a).uv(uv[0], uv[2]).endVertex();
            buffer.vertex(matrix, x, y, z + dz).color(r, g, b, a).uv(uv[0], uv[3]).endVertex();
            buffer.vertex(matrix, x + dx, y, z + dz).color(r, g, b, a).uv(uv[1], uv[3]).endVertex();
            buffer.vertex(matrix, x + dx, y, z).color(r, g, b, a).uv(uv[1], uv[2]).endVertex();
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void blitTexture(MultiBufferSource bufferSource, PoseStack poseStack, String name,
                            boolean transparency, float x, float y, float z, float dx, float dz,
                            int packedLight, float r, float g, float b, float a) {
        textures.get(name).ifPresent(entry -> {
            bindTexture();
            Matrix4f matrix = poseStack.last().pose();
            RenderType type = transparency ? renderTypeWithColorLightmapTransparency.get() : renderTypeWithColorLightmap.get();
            VertexConsumer buffer = bufferSource.getBuffer(type);
            float[] uv = entry.normalized(width, height);
            buffer.vertex(matrix, x, y, z).color(r, g, b, a).uv(uv[0], uv[2]).uv2(packedLight).endVertex();
            buffer.vertex(matrix, x, y, z + dz).color(r, g, b, a).uv(uv[0], uv[3]).uv2(packedLight).endVertex();
            buffer.vertex(matrix, x + dx, y, z + dz).color(r, g, b, a).uv(uv[1], uv[3]).uv2(packedLight).endVertex();
            buffer.vertex(matrix, x + dx, y, z).color(r, g, b, a).uv(uv[1], uv[2]).uv2(packedLight).endVertex();
        });
    }

    public ResourceLocation texture() {
        return texture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Entry<PointEntry> points() {
        return points;
    }

    public Entry<TextEntry> texts() {
        return texts;
    }

    public Entry<TextureEntry> textures() {
        return textures;
    }

    public Entry<SlotEntry> slot() {
        return slot;
    }

    public Entry<SlotsEntry> slots() {
        return slots;
    }

    public Entry<ProgressEntry> progress() {
        return progress;
    }

    public Entry<ButtonEntry> buttons() {
        return buttons;
    }

    public void reload() {
        TextureParser.reload(this);
    }

    public static final class Entry<T> implements Iterable<T> {
        private List<T> list = Collections.emptyList();
        private Map<String, T> map = Collections.emptyMap();

        public Optional<T> get(@Nullable String name) {
            if (name == null || name.isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(map.get(name));
        }

        public T ensureGet(String name) {
            return Objects.requireNonNull(map.get(name));
        }

        public boolean contains(String name) {
            return map.containsKey(name);
        }

        void set(List<T> list, Map<String, T> map) {
            this.list = List.copyOf(list);
            this.map = Map.copyOf(map);
        }

        @Override
        public Iterator<T> iterator() {
            return Iterators.unmodifiableIterator(list.iterator());
        }
    }
}
