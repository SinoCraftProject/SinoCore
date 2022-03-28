package games.moegirl.sinocraft.sinocore.common.blockentity;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.entity.BlockEntityType;
import games.moegirl.sinocraft.sinocore.common.block.ModBlockRegister;

public class ModBlockEntityRegister {

    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, "sinocore");

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
