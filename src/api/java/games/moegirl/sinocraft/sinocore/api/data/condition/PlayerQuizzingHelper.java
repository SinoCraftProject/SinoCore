package games.moegirl.sinocraft.sinocore.api.data.condition;

import games.moegirl.sinocraft.sinocore.api.capability.IQuizzingPlayer;
import games.moegirl.sinocraft.sinocore.api.capability.SCCapabilities;
import net.minecraft.world.entity.player.Player;

public class PlayerQuizzingHelper {
    public static boolean isCompleteSuccessfully(Player player, boolean detectSuccess) {
        var cap = player.getCapability(SCCapabilities.QUIZZING_PLAYER_CAPABILITY);

        if (!cap.isPresent()) {
            return false;
        }

        var quiz = cap.resolve().get();

        if (detectSuccess) {
            if (isEnded(quiz)) {
                return isSuccessful(quiz);
            } else {
                return false;
            }
        } else {
            return isEnded(quiz);
        }
    }

    public static boolean hasReachedMaxStage(IQuizzingPlayer quiz) {
        return quiz.getQuizStage() >= quiz.maxQuizStage();
    }

    public static boolean isEnded(IQuizzingPlayer quiz) {
        return hasReachedMaxStage(quiz) || !quiz.isQuizzing();
    }

    public static boolean isSuccessful(IQuizzingPlayer quiz) {
        return quiz.isSucceed();
    }
}
