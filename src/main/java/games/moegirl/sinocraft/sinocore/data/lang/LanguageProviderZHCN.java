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
        add(QuizCommand.MESSAGE_NOT_ENABLED, "问答挑战没有在本服务器启用。");
        add(QuizCommand.MESSAGE_SUCCEED, "恭喜你已经通过了问答挑战！");
        add(QuizCommand.MESSAGE_BROADCAST_SUCCEED, "玩家 %s 已经通过了问答挑战！");
        add(QuizCommand.MESSAGE_FAIL, "挑战失败！");
        add(QuizCommand.MESSAGE_WRONG_STATE, "错误的游戏状态！");
        add(QuizCommand.MESSAGE_STARTED, "问~答~开始！");
        add(QuizCommand.MESSAGE_QUESTION, "下面请听新一道问题：%s");
        add(QuizCommand.MESSAGE_QUESTION_LAST, "请踩在带有你认为正确的答案的选项的压力板上。");
        add(QuizCommand.MESSAGE_ANSWER_MARKED, "选项 %s、");
        add(QuizCommand.MESSAGE_ANSWER_WRONG, "回~答~错误！");
        add(QuizCommand.MESSAGE_ANSWER_RIGHT, "回答正确！");
    }
}
