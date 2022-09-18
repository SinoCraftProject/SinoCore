package games.moegirl.sinocraft.sinocore;

import cn.hutool.core.thread.ExecutorBuilder;
import games.moegirl.sinocraft.sinocore.api.ApiLoader;
import games.moegirl.sinocraft.sinocore.api.SinoCoreAPI;
import games.moegirl.sinocraft.sinocore.api.impl.Crafting;
import games.moegirl.sinocraft.sinocore.api.impl.Network;
import games.moegirl.sinocraft.sinocore.api.utility.json.JsonUtils;
import games.moegirl.sinocraft.sinocore.block.SCBlockItems;
import games.moegirl.sinocraft.sinocore.block.SCBlocks;
import games.moegirl.sinocraft.sinocore.block.blockentity.SCBlockEntities;
import games.moegirl.sinocraft.sinocore.config.QuizModelConfig;
import games.moegirl.sinocraft.sinocore.config.model.QuizModel;
import games.moegirl.sinocraft.sinocore.gui.SCMenus;
import games.moegirl.sinocraft.sinocore.item.SCItems;
import games.moegirl.sinocraft.sinocore.network.SCNetworks;
import games.moegirl.sinocraft.sinocore.utility.SCConstants;
import games.moegirl.sinocraft.sinocore.utility.json.serializer.FluidStackSerializer;
import games.moegirl.sinocraft.sinocore.utility.json.serializer.IngredientSerializer;
import games.moegirl.sinocraft.sinocore.utility.json.serializer.ItemStackSerializer;
import games.moegirl.sinocraft.sinocore.utility.json.serializer.NonNullListSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

@Mod(SinoCore.MODID)
public class SinoCore {
    private static final Logger LOGGER = LoggerFactory.getLogger("SinoCore");
    public static final String MODID = "sinocore";
    public static final String VERSION = "@version@";

    private static SinoCore INSTANCE = null;

    private final ThreadPoolExecutor EXECUTOR;

    public SinoCore() {
        LOGGER.info("Loading SinoCore. Ver: " + VERSION);

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
        bus.addListener(this::onClientSetup);
        SinoCoreAPI._loadCoreApi(this::registerApi);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, QuizModelConfig.SPEC, "sinoseries/sinocore/quiz.toml");

        JsonUtils.INSTANCE
                .registerAdapter(Ingredient.class, new IngredientSerializer())
                .registerAdapter(ItemStack.class, new ItemStackSerializer())
                .registerAdapter(FluidStack.class, new FluidStackSerializer())
                .registerAdapter(NonNullList.class, new NonNullListSerializer());

        LOGGER.info("SinoCore loaded!");
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

    private void onClientSetup(FMLClientSetupEvent event) {
        if (SCConstants.IS_DEV) {
            // Fixme: qyl27: Not working for debug show highlighted block shape box.
//            MinecraftForge.EVENT_BUS.register(new DebugBlockHighlighter());
        }
    }

    private void registerApi(ApiLoader loader) {
        loader.loadAll(MODID, Crafting.INSTANCE, Network.INSTANCE);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
