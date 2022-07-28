package games.moegirl.sinocraft.sinocore.api.utility.capability;

import games.moegirl.sinocraft.sinocore.api.SinoCoreAPI;
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
        var capOptional = original
                .getCapability(capability);
        if (!capOptional.isPresent()) {
            capOptional = provider.getCapability(capability);

            if (!capOptional.isPresent()) {
                SinoCoreAPI.LOGGER.warn("Cannot get capability for " + capability.getName());
                return;
            }
        }

        var cap = capOptional.orElseThrow(null);

        if (cap == null) {
            return;
        }

        var newCapOptional = newPlayer
                .getCapability(capability);
        if (!newCapOptional.isPresent()) {
            newCapOptional = provider.getCapability(capability);

            if (!capOptional.isPresent()) {
                SinoCoreAPI.LOGGER.warn("Cannot get capability for " + capability.getName());
                return;
            }
        }

        var newCap = capOptional.orElseThrow(null);

        if (newCap == null) {
            return;
        }

        newCap.deserializeNBT(cap.serializeNBT());
    }


    public static void clone(Player newPlayer, Player original,
                             ICapabilityProvider provider,
                             Capability<IPlayerCapability> capability) {
        clone(newPlayer, original, LazyOptional.empty(), provider, capability);
    }
}
