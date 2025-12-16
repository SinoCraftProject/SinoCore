package games.moegirl.sinocraft.sinocore.api.gui.layout.entry;

import java.util.List;

public abstract class SlotGroupEntry extends AbstractComponentEntry {
    protected SlotGroupEntry(AbstractComponentEntry entry) {
        super(entry);
    }

    public abstract List<SlotEntry> getSlots();

    public SlotEntry getSlot(int index) {
        return getSlots().get(index);
    }

    public int getSlotCount() {
        return getSlots().size();
    }
}
