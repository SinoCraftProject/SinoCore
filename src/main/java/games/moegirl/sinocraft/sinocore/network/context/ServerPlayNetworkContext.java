package games.moegirl.sinocraft.sinocore.network.context;

import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class ServerPlayNetworkContext extends ServerNetworkContext {
    @Getter
    private final ServerPlayer player;

    public ServerPlayNetworkContext(Connection connection, MinecraftServer server, ServerPlayer player) {
        super(ConnectionProtocol.PLAY, connection, server);
        this.player = player;
    }
}
