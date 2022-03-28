package games.moegirl.sinocraft.sinocore.block;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class ModBlockItemRegister {

    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, "sinocore");

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
