package games.moegirl.sinocraft.sinocore.api.utility.texture;

import com.google.common.collect.Iterators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
public final class TextureMap {
    private final ResourceLocation texture;
    public int width = 0;
    public int height = 0;
    private final Entry<PointEntry> points = new Entry<>();
    private final Entry<TextEntry> texts = new Entry<>();
    private final Entry<TextureEntry> textures = new Entry<>();
    private final Entry<SlotEntry> slot = new Entry<>();
    private final Entry<SlotsEntry> slots = new Entry<>();
    private final Entry<ProgressEntry> progress = new Entry<>();
    private final Entry<ButtonEntry> buttons = new Entry<>();

    TextureMap(ResourceLocation texture) {
        this.texture = texture;
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
