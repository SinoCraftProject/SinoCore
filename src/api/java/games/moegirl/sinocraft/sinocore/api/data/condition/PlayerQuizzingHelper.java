package games.moegirl.sinocraft.sinocore.api.data.condition;

import games.moegirl.sinocraft.sinocore.api.capability.SCCapabilities;
import net.minecraft.world.entity.player.Player;

public class PlayerQuizzingHelper {
    public static boolean isCorrect(Player player, String answer) {
        var cap = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY);

        if (!cap.isPresent()) {
            return false;
        }

        // Todo: qyl27.
        return false;
    }
}
