package games.moegirl.sinocraft.sinocore;

import games.moegirl.sinocraft.sinocore.api.ApiLoader;
import games.moegirl.sinocraft.sinocore.api.SinoCoreAPI;
import games.moegirl.sinocraft.sinocore.api.impl.Crafting;
import games.moegirl.sinocraft.sinocore.api.impl.Mixins;
import games.moegirl.sinocraft.sinocore.block.ModBlockItemRegister;
import games.moegirl.sinocraft.sinocore.block.ModBlockRegister;
import games.moegirl.sinocraft.sinocore.block.blockentity.ModBlockEntityRegister;
import games.moegirl.sinocraft.sinocore.crafting.IngredientRegister;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SinoCore.MODID)
public class SinoCore {
    public static final String MODID = "sinocore";

    public SinoCore() {
        SinoCoreAPI.load(this::registerApi);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlockRegister.register(bus);
        ModBlockItemRegister.register(bus);
        ModBlockEntityRegister.register(bus);

        bus.addListener(this::onSetup);
    }

    private void onSetup(FMLCommonSetupEvent event) {
        IngredientRegister.register();
    }

    private void registerApi(ApiLoader loader) {
        loader.setCrafting(Crafting.INSTANCE);
        loader.setMixins(Mixins.INSTANCE);
    }
}
