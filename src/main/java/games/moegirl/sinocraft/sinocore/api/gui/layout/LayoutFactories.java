package games.moegirl.sinocraft.sinocore.api.gui.layout;

import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.SlotEntry;
import games.moegirl.sinocraft.sinocore.api.gui.layout.entry.SlotGroupEntry;
import games.moegirl.sinocraft.sinocore.api.gui.menu.SlotStrategy;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

import java.util.*;
import java.util.function.Function;

public class LayoutFactories {

    // Todo: addSlot for AbstractContainerMenu, now you may do it manually.

    public static <C extends Container> Slot createSlot(SlotEntry entry, C container, int index,
                                                        SlotStrategy<C> slotStrategy) {
        return slotStrategy.createSlot(container, index, entry.getSlotX(), entry.getSlotY());
    }

    /**
     * Create slots of a container.
     * Usually used to fill players inventory or containers contents in menu.
     *
     * @param container    Container.
     * @param beginIndex   Begin index in container.
     * @param slotStrategy A {@link SlotStrategy} of all the slots.
     */
    public static <C extends Container> List<Slot> createSlots(SlotGroupEntry entry, C container, int beginIndex,
                                                            SlotStrategy<C> slotStrategy) {
        return createSlots(entry, container, beginIndex, i -> slotStrategy);
    }

    /**
     * Create slots with a slot strategy getter.
     * If you have a handheld container, you will hope players can't move the item out.
     *
     * @param container          Container.
     * @param beginIndex         Begin index in container.
     * @param slotStrategyGetter Get {@link SlotStrategy} by slot id.
     */
    public static <C extends Container> List<Slot> createSlots(SlotGroupEntry entry, C container, int beginIndex,
                                                            Function<Integer, SlotStrategy<C>> slotStrategyGetter) {
        var list = new ArrayList<Slot>();
        for (int i = 0; i < entry.getSlotCount(); i++) {
            SlotEntry e = entry.getSlot(i);
            var index = beginIndex + i;
            list.add(createSlot(e, container, index, slotStrategyGetter.apply(index)));
        }
        return list;
    }
}
