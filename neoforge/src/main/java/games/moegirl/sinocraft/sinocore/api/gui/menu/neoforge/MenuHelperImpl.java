package games.moegirl.sinocraft.sinocore.api.gui.menu.neoforge;

import games.moegirl.sinocraft.sinocore.api.gui.menu.IExtraDataMenuProvider;
import net.minecraft.server.level.ServerPlayer;

public class MenuHelperImpl {
    public static void openMenuWithData(ServerPlayer player, IExtraDataMenuProvider provider) {
        player.openMenu(provider, provider::writeExtraData);
    }
}
