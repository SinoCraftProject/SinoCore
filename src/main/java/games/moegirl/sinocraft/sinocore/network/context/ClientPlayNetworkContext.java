package games.moegirl.sinocraft.sinocore.network.context;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;

public final class ClientPlayNetworkContext extends ClientNetworkContext {
    @Getter
    private final LocalPlayer player;

    public ClientPlayNetworkContext(Connection connection, Minecraft minecraft, LocalPlayer player) {
        super(ConnectionProtocol.PLAY, connection, minecraft);
        this.player = player;
    }
}
