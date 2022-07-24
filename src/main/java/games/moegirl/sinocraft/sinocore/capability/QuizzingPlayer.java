package games.moegirl.sinocraft.sinocore.capability;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import games.moegirl.sinocraft.sinocore.api.capability.IQuizzingPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.HashMap;
import java.util.List;

public class QuizzingPlayer implements IQuizzingPlayer {
    protected boolean inQuiz = false;
    protected int quizStage = 0;
    protected String question = "";
    protected BiMap<String, Boolean> answers = HashBiMap.create(new HashMap<>());

    @Override
    public boolean isQuizzing() {
        return inQuiz;
    }

    @Override
    public void setQuizzing(boolean quizzing) {
        inQuiz = quizzing;
    }

    @Override
    public int getQuizStage() {
        return quizStage;
    }

    @Override
    public void setQuizStage(int stage) {
        quizStage = stage;
    }

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(String questionIn) {
        question = questionIn;
    }

    @Override
    public List<String> getAnswers() {
        return answers.keySet().stream().toList();
    }

    @Override
    public void addAnswer(String answer, boolean isCorrect) {
        answers.put(answer, isCorrect);
    }

    @Override
    public void clearAnswers() {
        answers.clear();
    }

    @Override
    public String getCorrectAnswer() {
        return answers.inverse().get(true);
    }

    @Override
    public boolean isCorrect(String answer) {
        return answers.get(answer);
    }

    @Override
    public CompoundTag serializeNBT() {
        var nbt = new CompoundTag();

        var quiz = new CompoundTag();
        quiz.putBoolean("inQuiz", inQuiz);
        quiz.putInt("stage", quizStage);
        quiz.putString("question", question);

        var answersList = new ListTag();
        for (var answer : answers.entrySet()) {
            var ans = new CompoundTag();
            ans.putString("answer", answer.getKey());
            ans.putBoolean("isCorrect", answer.getValue());
            answersList.add(ans);
        }
        nbt.put("answers", answersList);

        nbt.put("quiz", quiz);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (!nbt.contains("quiz")) {
            return;
        }

        var quiz = nbt.getCompound("quiz");

        if (quiz.contains("inQuiz")) {
            inQuiz = quiz.getBoolean("inQuiz");
        }

        if (quiz.contains("stage")) {
            quizStage = quiz.getInt("stage");
        }

        if (quiz.contains("question")) {
            question = quiz.getString("question");
        }

        if (quiz.contains("answers")) {
            for (var answer : quiz.getList("answers", 10)) {
                if (answer instanceof CompoundTag ans) {
                    if (ans.contains("answer") && ans.contains("isCorrect")) {
                        answers.put(ans.getString("answer"), ans.getBoolean("isCorrect"));
                    }
                }
            }
        }
    }
}
