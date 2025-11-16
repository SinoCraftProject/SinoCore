package games.moegirl.sinocraft.sinocore_test.fabric;

import games.moegirl.sinocraft.sinocore_test.SinoCoreTest;
import net.fabricmc.api.ModInitializer;

public class SinoCoreTestFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SinoCoreTest.registerAll();
    }
}
