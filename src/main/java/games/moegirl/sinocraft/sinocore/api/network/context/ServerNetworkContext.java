package games.moegirl.sinocraft.sinocore.api.network.context;

import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.server.MinecraftServer;

public sealed class ServerNetworkContext extends NetworkContext permits ServerConfigurationNetworkContext, ServerPlayNetworkContext {
    @Getter
    protected MinecraftServer server;

    public ServerNetworkContext(ConnectionProtocol stage, Connection connection, MinecraftServer server) {
        super(stage, connection);
        this.server = server;
    }
}
