package games.moegirl.sinocraft.sinocore.config;

import games.moegirl.sinocraft.sinocore.config.model.QuizModel;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class QuizModelConfig {
    public static final Config CONFIG;
    public static final ForgeConfigSpec SPEC;

    static {
        final Pair<Config, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
                .configure(Config::new);

        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }

    public static class Config {
        public final ForgeConfigSpec.IntValue MAX_STAGE;
        public final ForgeConfigSpec.BooleanValue ENABLED;
        public final ForgeConfigSpec.ConfigValue<String> DATA_URL;

        Config(ForgeConfigSpec.Builder builder) {
            builder.push("quiz");

            MAX_STAGE = builder.worldRestart().defineInRange("maxStage", 15, 3, 50);
            ENABLED = builder.worldRestart().define("enabled", false);
            DATA_URL = builder.worldRestart().define("url", "https://quiz.sino.moegirl.games/api/quiz/something");

            builder.pop();
        }
    }
}
