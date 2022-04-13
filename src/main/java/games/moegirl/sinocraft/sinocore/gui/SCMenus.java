package games.moegirl.sinocraft.sinocore.gui;

import games.moegirl.sinocraft.sinocore.SinoCore;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SCMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SinoCore.MODID);

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}