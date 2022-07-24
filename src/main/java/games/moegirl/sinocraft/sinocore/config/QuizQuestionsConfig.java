package games.moegirl.sinocraft.sinocore.config;

import com.google.common.collect.BiMap;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionsConfig {
    public static ForgeConfigSpec CONFIG;

    public static final Pair<QuizQuestionsConfig, ForgeConfigSpec> PAIR = new ForgeConfigSpec.Builder()
            .configure(QuizQuestionsConfig::new);

    public static ForgeConfigSpec.IntValue MAX_STAGE;
    public static ForgeConfigSpec.ConfigValue<List<? extends QuestionEntry>> QUESTIONS;

    public static List<? extends QuestionEntry> DEFAULT_QUESTIONS = new ArrayList<>();

    QuizQuestionsConfig(ForgeConfigSpec.Builder builder) {
        fillDefault();
        builder.push("quiz");

        MAX_STAGE = builder.defineInRange("maxStage", 15, 3, 25);
        QUESTIONS = builder.defineList("questions", new ArrayList<>(), o -> {
            return true;
        });

        builder.pop();

        CONFIG = builder.build();
    }

    public static void fillDefault() {
//        DEFAULT_QUESTIONS.add();
    }

    public static record QuestionEntry(String question, BiMap<String, Boolean> map) {

    }
}
