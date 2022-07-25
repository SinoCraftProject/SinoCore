package games.moegirl.sinocraft.sinocore.data.lang;

import games.moegirl.sinocraft.sinocore.api.data.I18nProviderBase;
import games.moegirl.sinocraft.sinocore.command.QuizCommand;
import net.minecraft.data.DataGenerator;

public class LanguageProviderENUS extends I18nProviderBase {
    public LanguageProviderENUS(DataGenerator genIn, String modIdIn, String localeIn) {
        super(genIn, modIdIn, modIdIn, localeIn);
    }

    @Override
    protected void addTranslations() {
        add(QuizCommand.MESSAGE_NOT_PLAYER, "Please use this command with a player!");
        add(QuizCommand.MESSAGE_NOT_STARTED, "Quiz is not started!");
        add(QuizCommand.MESSAGE_NOT_ENABLED, "Quiz is not enabled!");
        add(QuizCommand.MESSAGE_SUCCEED, "Congratulations! You have passed the quiz.");
        add(QuizCommand.MESSAGE_BROADCAST_SUCCEED, "Player %s passed the quiz！");
        add(QuizCommand.MESSAGE_FAIL, "You failed the quiz.");
        add(QuizCommand.MESSAGE_WRONG_STATE, "Wrong game state!");
        add(QuizCommand.MESSAGE_STARTED, "Quiz started!");
        add(QuizCommand.MESSAGE_QUESTION, "Now listen to the question: %s");
        add(QuizCommand.MESSAGE_QUESTION_LAST, "Please step on the corresponding pressure plate.");
        add(QuizCommand.MESSAGE_ANSWER_MARKED, "%s. ");
        add(QuizCommand.MESSAGE_ANSWER_WRONG, "Ah-oh, the answer is wrong.");
        add(QuizCommand.MESSAGE_ANSWER_RIGHT, "Good answer.");
    }
}
