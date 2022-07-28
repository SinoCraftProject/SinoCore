package games.moegirl.sinocraft.sinocore.api.utility.capability;

import games.moegirl.sinocraft.sinocore.api.capability.IPlayerCapability;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityHelper {
    public static void clone(Player newPlayer, Player original,
                             LazyOptional<IPlayerCapability> optional,
                             ICapabilityProvider provider,
                             Capability<IPlayerCapability> capability) {
        var cap = original
                .getCapability(capability)
                .orElse(provider
                        .getCapability(capability)
                        .orElseThrow(() -> new RuntimeException("Cannot get capability " + capability.getName())));
        var newCap = newPlayer
                .getCapability(capability)
                .orElse(provider
                        .getCapability(capability)
                        .orElseThrow(() -> new RuntimeException("Cannot get capability " + capability.getName())));

        newCap.deserializeNBT(cap.serializeNBT());
    }
}
