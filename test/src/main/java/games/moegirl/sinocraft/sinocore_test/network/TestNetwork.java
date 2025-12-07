package games.moegirl.sinocraft.sinocore_test.network;

import games.moegirl.sinocraft.sinocore.api.network.NetworkManager;
import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;

public class TestNetwork {

    public static IRegRef<TestNetworkItem> ITEM;

    public static void registerAll() {

        NetworkManager.playPacket(C2SHelloPacket.TYPE)
                .codec(C2SHelloPacket.STREAM_CODEC)
                .serverHandler(C2SHelloPacket::handle)
                .register();

        NetworkManager.playPacket(S2CHelloPacket.TYPE)
                .codec(S2CHelloPacket.STREAM_CODEC)
                .clientHandler(S2CHelloPacket::handle)
                .register();

        ITEM = TestRegistry.ITEMS.register("test_network", TestNetworkItem::new);
    }
}
