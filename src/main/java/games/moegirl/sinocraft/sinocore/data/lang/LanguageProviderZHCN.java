package games.moegirl.sinocraft.sinocore.data.lang;

import games.moegirl.sinocraft.sinocore.api.data.I18nProviderBase;
import games.moegirl.sinocraft.sinocore.command.QuizCommand;
import net.minecraft.data.DataGenerator;

public class LanguageProviderZHCN extends I18nProviderBase {
    public LanguageProviderZHCN(DataGenerator genIn, String modIdIn, String localeIn) {
        super(genIn, modIdIn, modIdIn, localeIn);
    }

    @Override
    protected void addTranslations() {
        add(QuizCommand.MESSAGE_NOT_PLAYER, "这个命令只能由玩家使用！");
        add(QuizCommand.MESSAGE_NOT_STARTED, "问答还没有开始！");
        add(QuizCommand.MESSAGE_SUCCEED, "恭喜你已经通过了问答！");
        add(QuizCommand.MESSAGE_FAIL, "回~答~错误！");
        add(QuizCommand.MESSAGE_WRONG_STATE, "错误的游戏状态！");
        add(QuizCommand.MESSAGE_QUESTION, "下面请听新一道问题：%s");
        add(QuizCommand.MESSAGE_QUESTION_LAST, "请踩在带有你认为正确的答案的选项的压力板上。");
        add(QuizCommand.MESSAGE_ANSWER_MARKED, "选项 %s、");
    }
}
