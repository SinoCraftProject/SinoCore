package games.moegirl.sinocraft.sinocore.api.gui.layout;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import games.moegirl.sinocraft.sinocore.SinoCorePlatform;
import games.moegirl.sinocraft.sinocore.api.gui.GuiImage;
import games.moegirl.sinocraft.sinocore.api.gui.GuiTexture;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.*;
import games.moegirl.sinocraft.sinocore.api.gui.screen.AbstractMenuScreen;
import games.moegirl.sinocraft.sinocore.api.gui.screen.AbstractScreen;
import games.moegirl.sinocraft.sinocore.api.util.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class LayoutManager {

    private static final Map<String, MapCodec<? extends AbstractComponentEntry>> CODEC_MAP = new HashMap<>();
    private static final Map<Class<?>, String> CODEC_NAME_MAP = new HashMap<>();

    public static final Codec<AbstractComponentEntry> COMPONENTS_CODEC = Codec.STRING.dispatch(LayoutManager::getTypeName, CODEC_MAP::get);

    /**
     * 每次调用 loadWidgets 都重新从硬盘加载
     */
    public static boolean FORCE_RELOAD_WIDGETS = SinoCorePlatform.isDevelopmentEnvironment();

    static {
        addComponent("button", ButtonEntry.MAP_CODEC, ButtonEntry.class);
        addComponent("edit_box", EditBoxEntry.MAP_CODEC, EditBoxEntry.class);
        addComponent("image_button", ImageButtonEntry.MAP_CODEC, ImageButtonEntry.class);
        addComponent("progress_bar", ProgressBarEntry.MAP_CODEC, ProgressBarEntry.class);
        addComponent("rectangle", RectangleEntry.MAP_CODEC, RectangleEntry.class);
        addComponent("slot", SlotEntry.MAP_CODEC, SlotEntry.class);
        addComponent("slot_list", SlotListEntry.MAP_CODEC, SlotListEntry.class);
        addComponent("slot_grid", SlotGridEntry.MAP_CODEC, SlotGridEntry.class);
        addComponent("image", ImageEntry.MAP_CODEC, ImageEntry.class);
        addComponent("text", TextEntry.MAP_CODEC, TextEntry.class);
    }
    private static <T extends AbstractComponentEntry> void addComponent(String name, MapCodec<T> codec, Class<T> clazz) {
        CODEC_MAP.put(name, codec);
        CODEC_NAME_MAP.put(clazz, name);
    }

    private static String getTypeName(AbstractComponentEntry entry) {
        return CODEC_NAME_MAP.get(entry.getClass());
    }

    private static final BiMap<ResourceLocation, Layout> LAYOUTS = HashBiMap.create();

    /**
     * 从 GUI json 文件加载 Widgets 对象
     *
     * @param name 文件路径
     * @return 从 resources 中加载的 Widgets 对象
     */
    public static Layout loadWidgets(ResourceLocation name) {
        if (!FORCE_RELOAD_WIDGETS && LAYOUTS.containsKey(name)) {
            return (LAYOUTS.get(name));
        }
        ResourceLocation jsonFile = ResourceLocation.fromNamespaceAndPath(name.getNamespace(), name.getPath() + ".json");
        Resource resource = ResourceManagerHelper.getResourceManager()
                .getResource(jsonFile)
                .orElseThrow(() -> new RuntimeException("Failed to load widget " + name + ": " + jsonFile + " not found"));
        try (BufferedReader reader = resource.openAsReader()) {
            var jsonElement = JsonParser.parseReader(reader);
            var layout = Layout.CODEC.decode(JsonOps.INSTANCE, jsonElement)
                    .getOrThrow(err -> {
                        throw new RuntimeException("Failed to load widget " + name + ": " + err);
                    })
                    .getFirst();
            LAYOUTS.put(name, layout);
            return layout;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load widget " + name + " (" + jsonFile + ")", e);
        }
    }

    public static GuiImage getBackground(Layout layout) {
        var background = layout.getBackground();
        if (background != null) {
            return background;
        }
        var name = LAYOUTS.inverse().get(layout);
        var path = ResourceLocation.fromNamespaceAndPath(name.getNamespace(), name.getPath() + ".png");
        return new GuiTexture(path);
    }

    /**
     * 重新加载所有 json 文件
     */
    public static void reloadAllWidgets() {
        List<ResourceLocation> names = new ArrayList<>(LAYOUTS.keySet());
        LAYOUTS.clear();
        for (ResourceLocation name : names) {
            loadWidgets(name);
        }
    }

    public static AbstractScreen createLayoutScreen(Layout layout) {
        var screen = new AbstractScreen(layout.getTitle());
        var layoutWindow = new LayoutWindow(screen, layout);
        screen.addWindow(layoutWindow, true);
        return screen;
    }

    public static <T extends AbstractContainerMenu> AbstractMenuScreen<T> createLayoutMenuScreen(T menu, Inventory playerInventory, Layout layout) {
        var screen = new AbstractMenuScreen<>(menu, playerInventory, layout.getTitle());
        var layoutWindow = new LayoutWindow(screen, layout);
        screen.addWindow(layoutWindow, true);
        return screen;
    }
}
