package games.moegirl.sinocraft.sinocore.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class QuizQuestionsConfig {
    public static ForgeConfigSpec CONFIG;

    public static final Pair<QuizQuestionsConfig, ForgeConfigSpec> PAIR = new ForgeConfigSpec.Builder()
            .configure(QuizQuestionsConfig::new);

    QuizQuestionsConfig(ForgeConfigSpec.Builder builder) {



        CONFIG = builder.build();
    }
}
