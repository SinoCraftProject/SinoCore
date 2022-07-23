package games.moegirl.sinocraft.sinocore.api.woodwork;

import net.minecraft.core.BlockPos;

public class SignEditHelper {
    public static void editSign(BlockPos pos) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.level != null && mc.level.getBlockEntity(pos) instanceof ModSignBlockEntity sign) {
            mc.setScreen(new ModSignEditScreen(sign, mc.isTextFilteringEnabled()));
        }
    }
}
