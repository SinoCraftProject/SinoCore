package games.moegirl.sinocraft.sinocore.api.data.condition;

import games.moegirl.sinocraft.sinocore.api.capability.IQuizzingPlayer;
import games.moegirl.sinocraft.sinocore.api.capability.SCCapabilities;
import net.minecraft.world.entity.player.Player;

public class PlayerQuizzingHelper {
    public static boolean isCompleteSuccessfully(Player player, boolean detectSuccess, boolean lastCorrect) {
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
            if (lastCorrect) {
                return !isEnded(quiz) && !isSuccessful(quiz);
            } else {
                if (isEnded(quiz)) {
                    return isFailed(quiz);
                } else {
                    return false;
                }
            }
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

    public static boolean isFailed(IQuizzingPlayer quiz) {
        return !quiz.isSucceed();
    }
}
