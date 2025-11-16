package games.moegirl.sinocraft.sinocore_test.neoforge;

import games.moegirl.sinocraft.sinocore_test.SinoCoreTest;
import net.neoforged.fml.common.Mod;

@Mod(SinoCoreTest.MODID)
public class SinoCoreTestNeoForge {

    public SinoCoreTestNeoForge() {
        SinoCoreTest.registerAll();
    }
}
