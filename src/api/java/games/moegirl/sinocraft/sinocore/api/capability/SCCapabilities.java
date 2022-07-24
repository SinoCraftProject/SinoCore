package games.moegirl.sinocraft.sinocore.api.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * All capability
 */
public class SCCapabilities {
    // Todo: qyl27: not registered yet.
    public static final Capability<IHeat> HEAT = CapabilityManager.get(new CapabilityToken<>() { });

    public static final Capability<IQuizzingPlayer> QUIZZING_PLAYER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });
}
