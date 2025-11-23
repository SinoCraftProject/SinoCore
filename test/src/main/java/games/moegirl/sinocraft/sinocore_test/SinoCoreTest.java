package games.moegirl.sinocraft.sinocore_test;

import games.moegirl.sinocraft.sinocore_test.datagen.TestDataItem;
import games.moegirl.sinocraft.sinocore_test.network.TestNetwork;
import games.moegirl.sinocraft.sinocore_test.registry.TestRegistry;

public class SinoCoreTest {
    public static final String MODID = "sinocore_test";

    public static void registerAll() {
        TestRegistry.registerAll();
        TestNetwork.registerAll();
        TestDataItem.registerAll();
    }
}
