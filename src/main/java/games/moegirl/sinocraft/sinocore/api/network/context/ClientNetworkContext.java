package games.moegirl.sinocraft.sinocore.api.network.context;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;

public abstract sealed class ClientNetworkContext extends NetworkContext permits ClientConfigurationNetworkContext, ClientPlayNetworkContext {
    @Getter
    protected final Minecraft minecraft;

    public ClientNetworkContext(ConnectionProtocol stage, Connection connection, Minecraft minecraft) {
        super(stage, connection);
        this.minecraft = minecraft;
    }
}
