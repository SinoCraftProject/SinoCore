package games.moegirl.sinocraft.sinocore.neoforge.util;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

import java.util.Objects;

public class ModBusHelper {
    public static IEventBus getModBus(String modId) {
        if ("minecraft".equals(modId)) {
            throw new RuntimeException("Hey, Minecraft has no event bus!");
        }

        return Objects.requireNonNull(ModList.get().getModContainerById(modId).orElseThrow().getEventBus());
    }
}
