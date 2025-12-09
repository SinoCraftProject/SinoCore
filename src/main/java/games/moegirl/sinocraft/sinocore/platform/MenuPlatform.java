package games.moegirl.sinocraft.sinocore.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import games.moegirl.sinocraft.sinocore.api.gui.menu.IExtraDataMenuProvider;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MenuPlatform {
    @ExpectPlatform
    public static void openMenuWithData(ServerPlayer player, IExtraDataMenuProvider provider) {
        throw new AssertionError();
    }
}
