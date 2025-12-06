package games.moegirl.sinocraft.sinocore.network.context;

import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.server.MinecraftServer;

public final class ServerConfigurationNetworkContext extends ServerNetworkContext {
    public ServerConfigurationNetworkContext(Connection connection, MinecraftServer server) {
        super(ConnectionProtocol.CONFIGURATION, connection, server);
    }
}
