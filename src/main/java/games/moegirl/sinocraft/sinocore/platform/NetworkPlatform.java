package games.moegirl.sinocraft.sinocore.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import games.moegirl.sinocraft.sinocore.api.network.PacketBuilder;
import games.moegirl.sinocraft.sinocore.api.network.PacketTarget;
import games.moegirl.sinocraft.sinocore.api.network.context.ClientPlayNetworkContext;
import games.moegirl.sinocraft.sinocore.api.network.context.ServerPlayNetworkContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class NetworkPlatform {
    @ExpectPlatform
    public static <T extends CustomPacketPayload> void send(T payload, ServerPlayer player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends CustomPacketPayload> void send(T payload, PacketTarget target) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends CustomPacketPayload> void sendToServer(T payload) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends CustomPacketPayload> void registerPlay(PacketBuilder<T, RegistryFriendlyByteBuf, ClientPlayNetworkContext, ServerPlayNetworkContext> packet) {
        throw new AssertionError();
    }
}
