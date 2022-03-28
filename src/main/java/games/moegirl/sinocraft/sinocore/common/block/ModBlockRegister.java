package games.moegirl.sinocraft.sinocore.common.block;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.Block;

public class ModBlockRegister {

    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, "sinocore");

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
