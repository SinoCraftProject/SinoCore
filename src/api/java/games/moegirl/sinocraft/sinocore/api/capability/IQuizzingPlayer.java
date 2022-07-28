package games.moegirl.sinocraft.sinocore.api.capability;

import oshi.util.tuples.Pair;

import java.util.List;

/**
 * Player quiz capability.
 *
 * @author qyl27
 */
public interface IQuizzingPlayer extends IPlayerCapability {
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

    List<Pair<String, String>> getAnswers();
    void addAnswer(String answer, String mark, boolean isCorrect);
    void clearAnswers();

    String getCorrectMark();
    boolean isCorrect(String mark);
}
