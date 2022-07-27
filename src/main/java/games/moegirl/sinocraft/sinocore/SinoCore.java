package games.moegirl.sinocraft.sinocore;

import cn.hutool.core.thread.ExecutorBuilder;
import games.moegirl.sinocraft.sinocore.api.ApiLoader;
import games.moegirl.sinocraft.sinocore.api.SinoCoreAPI;
import games.moegirl.sinocraft.sinocore.api.impl.Crafting;
import games.moegirl.sinocraft.sinocore.api.impl.Network;
import games.moegirl.sinocraft.sinocore.block.SCBlockItems;
import games.moegirl.sinocraft.sinocore.block.SCBlocks;
import games.moegirl.sinocraft.sinocore.block.blockentity.SCBlockEntities;
import games.moegirl.sinocraft.sinocore.config.QuizModelConfig;
import games.moegirl.sinocraft.sinocore.config.model.QuizModel;
import games.moegirl.sinocraft.sinocore.gui.SCMenus;
import games.moegirl.sinocraft.sinocore.item.SCItems;
import games.moegirl.sinocraft.sinocore.network.SCNetworks;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Mod(SinoCore.MODID)
public class SinoCore {
    private static final Logger logger = LogManager.getLogger("SinoCore");
    public static final String MODID = "sinocore";
    public static final String VERSION = "1.2.7";

    private static SinoCore INSTANCE = null;

    private final ThreadPoolExecutor EXECUTOR;

    public SinoCore() {
        logger.info("Loading SinoCore. Ver: " + VERSION);

        INSTANCE = this;
        EXECUTOR = ExecutorBuilder.create()
                .setMaxPoolSize(8)
                .setKeepAliveTime(60L * 1000 * 1000 * 1000)
                .build();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        SCNetworks.register();
        SCItems.register(bus);
        SCBlocks.register(bus);
        SCBlockItems.register(bus);
        SCBlockEntities.register(bus);

        SCMenus.register(bus);

        bus.addListener(this::onSetup);
        SinoCoreAPI._loadCoreApi(this::registerApi);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, QuizModelConfig.SPEC, "sinoseries/sinocore/quiz.toml");
    }

    public static SinoCore getInstance() {
        return INSTANCE;
    }

    public ThreadPoolExecutor getPool() {
        return EXECUTOR;
    }

    private void onSetup(FMLCommonSetupEvent event) {
        if (QuizModelConfig.CONFIG.ENABLED.get()) {
            QuizModel.fetch();
        }
    }

    private void registerApi(ApiLoader loader) {
        loader.loadAll(MODID, Crafting.INSTANCE, Network.INSTANCE);
    }

    public static Logger getLogger() {
        return logger;
    }
}
