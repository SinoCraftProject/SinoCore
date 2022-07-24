package games.moegirl.sinocraft.sinocore.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

/**
 * Player quiz capability.
 *
 * @author qyl27
 */
public interface IQuizzingPlayer extends INBTSerializable<CompoundTag> {
    boolean isQuizzing();
    void setQuizzing(boolean quizzing);
    boolean isSucceed();
    void setSucceed(boolean isSucceed);

    int getQuizStage();
    int maxQuizStage();
    void setQuizStage(int count);
    void setMaxQuizStage(int count);

    String getQuestion();
    void setQuestion(String question);

    List<String> getAnswers();
    void addAnswer(String answer, boolean isCorrect);
    void clearAnswers();

    String getCorrectAnswer();
    boolean isCorrect(String answer);
}
