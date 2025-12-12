package games.moegirl.sinocraft.sinocore.api.gui.widgets;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import games.moegirl.sinocraft.sinocore.SinoCorePlatform;
import games.moegirl.sinocraft.sinocore.api.gui.layout.widget.*;
import games.moegirl.sinocraft.sinocore.api.util.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class WidgetLoader {

    private static final Map<String, MapCodec<? extends AbstractWidgetEntry>> CODEC_MAP = new HashMap<>();
    private static final Map<Class<?>, String> CODEC_NAME_MAP = new HashMap<>();

    public static final Codec<AbstractWidgetEntry> WIDGET_CODEC = Codec.STRING.dispatch(WidgetLoader::getTypeName, CODEC_MAP::get);

    /**
     * 每次调用 loadWidgets 都重新从硬盘加载
     */
    public static boolean FORCE_RELOAD_WIDGETS = SinoCorePlatform.isDevelopmentEnvironment();

    static {
        addWidget("button", ButtonEntry.MAP_CODEC, ButtonEntry.class);
        addWidget("image_button", ImageButtonEntry.MAP_CODEC, ImageButtonEntry.class);
        addWidget("edit_box", EditBoxEntry.MAP_CODEC, EditBoxEntry.class);
        addWidget("point", PointEntry.MAP_CODEC, PointEntry.class);
        addWidget("progress", ProgressEntry.MAP_CODEC, ProgressEntry.class);
        addWidget("rect", RectangleEntry.MAP_CODEC, RectangleEntry.class);
        addWidget("slot", SlotEntry.CODEC, SlotEntry.class);
        addWidget("slots", SlotGroupEntry.CODEC, SlotGroupEntry.class);
        addWidget("text", TextEntry.CODEC, TextEntry.class);
        addWidget("texture", TextureEntry.CODEC, TextureEntry.class);
    }

    private static <T extends AbstractWidgetEntry> void addWidget(String name, MapCodec<T> codec, Class<T> clazz) {
        CODEC_MAP.put(name, codec);
        CODEC_NAME_MAP.put(clazz, name);
    }

    private static final Map<ResourceLocation, Widgets> WIDGETS = new HashMap<>();

    /**
     * 从 GUI json 文件加载 Widgets 对象
     *
     * @param name 文件路径
     * @return 从 resources 中加载的 Widgets 对象
     */
    public static Widgets loadWidgets(ResourceLocation name) {
        if (!FORCE_RELOAD_WIDGETS && WIDGETS.containsKey(name)) return (WIDGETS.get(name));
        ResourceLocation jsonFile = ResourceLocation.fromNamespaceAndPath(name.getNamespace(), name.getPath() + ".json");
        Resource resource = ResourceManagerHelper.getResourceManager()
                .getResource(jsonFile)
                .orElseThrow(() -> new RuntimeException("Failed to load widget " + name + ": " + jsonFile + " not found"));
        try (BufferedReader reader = resource.openAsReader()) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            Widgets widgets = Widgets.CODEC.decode(JsonOps.INSTANCE, jsonElement)
                    .getOrThrow(err -> {
                        throw new RuntimeException("Failed to load widget " + name + ": " + err);
                    })
                    .getFirst();
            widgets.setTexture(ResourceLocation.fromNamespaceAndPath(name.getNamespace(), name.getPath() + ".png"));
            WIDGETS.put(name, widgets);
            return widgets;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load widget " + name + " (" + jsonFile + ")", e);
        }
    }

    /**
     * 重新加载所有 json 文件
     */
    public static void reloadAllWidgets() {
        List<ResourceLocation> names = new ArrayList<>(WIDGETS.keySet());
        WIDGETS.clear();
        for (ResourceLocation name : names) {
            loadWidgets(name);
        }
    }

    public static String getTypeName(AbstractWidgetEntry entry) {
        return CODEC_NAME_MAP.get(entry.getClass());
    }
}
