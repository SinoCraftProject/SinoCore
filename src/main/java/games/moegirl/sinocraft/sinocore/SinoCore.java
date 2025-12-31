package games.moegirl.sinocraft.sinocore;

import games.moegirl.sinocraft.sinocore.advancement.criterion.SinoCoreCriteriaTriggers;
import games.moegirl.sinocraft.sinocore.api.data.DataTypes;
import games.moegirl.sinocraft.sinocore.command.SinoCoreCommands;
import games.moegirl.sinocraft.sinocore.utility.BuildInfo;

public class SinoCore {
    public static final String MODID = "sinocore";

    public SinoCore() {
        BuildInfo.printBuildInfo();

        DataTypes.register();
        SinoCoreCriteriaTriggers.register();
        SinoCoreCommands.register();
    }

    public void init() {
    }

    /**
     * Something must be run in the Main Thread, we called it as setup.
     * In Forge-like, it should be invoked in FMLCommonSetupEvent.
     */
    public void setup() {
    }
}
