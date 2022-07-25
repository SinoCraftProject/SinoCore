package games.moegirl.sinocraft.sinocore.config;

import games.moegirl.sinocraft.sinocore.config.model.QuizModel;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class QuizQuestionsConfig {
    public static QuizModel QUESTIONS;

    public static ForgeConfigSpec CONFIG;

    public static final Pair<QuizQuestionsConfig, ForgeConfigSpec> PAIR = new ForgeConfigSpec.Builder()
            .configure(QuizQuestionsConfig::new);

    public static ForgeConfigSpec.IntValue MAX_STAGE;
    public static ForgeConfigSpec.BooleanValue ENABLED;
    public static ForgeConfigSpec.ConfigValue<String> DATA_URL;

    QuizQuestionsConfig(ForgeConfigSpec.Builder builder) {
        builder.push("quiz");

        MAX_STAGE = builder.defineInRange("maxStage", 15, 3, 50);
        ENABLED = builder.define("enabled", true);
        DATA_URL = builder.define("url", "https://quiz.sino.moegirl.games/api/quiz");

        builder.pop();

        CONFIG = builder.build();

        QUESTIONS = QuizModel.fetch();
    }
}
